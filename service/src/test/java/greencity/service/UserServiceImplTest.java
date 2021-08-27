package greencity.service;

import greencity.ModelUtils;
import greencity.constant.ErrorMessage;
import greencity.dto.user.UserRoleDto;
import greencity.dto.user.UserVO;
import greencity.entity.User;
import greencity.enums.EmailNotification;
import greencity.enums.Role;
import greencity.exception.exceptions.BadUpdateRequestException;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.WrongIdException;
import greencity.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static greencity.enums.UserStatus.ACTIVATED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepo userRepo;
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private ModelMapper modelMapper;

    private UserVO userVO = UserVO.builder()
        .id(1L)
        .name("Test Testing")
        .email("test@gmail.com")
        .role(Role.ROLE_USER)
        .userStatus(ACTIVATED)
        .emailNotification(EmailNotification.DISABLED)
        .lastActivityTime(LocalDateTime.of(2020, 10, 10, 20, 10, 10))
        .dateOfRegistration(LocalDateTime.now())
        .socialNetworks(new ArrayList<>())
        .build();

    @Test
    void findByIdTest() {
        Long id = 1L;

        User user = new User();
        user.setId(1L);

        when(userRepo.findById(id)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserVO.class)).thenReturn(userVO);
        assertEquals(userVO, userService.findById(id));
        verify(userRepo, times(1)).findById(id);
    }

    @Test
    void checkIfTheUserIsOnlineExceptionTest() {
        assertThrows(WrongIdException.class, () -> userService.checkIfTheUserIsOnline(null));
    }

    @Test
    void checkIfTheUserIsOnlineEqualsTrueTest() {
        ReflectionTestUtils.setField(userService, "timeAfterLastActivity", 300000);
        Timestamp userLastActivityTime = Timestamp.valueOf(LocalDateTime.now());
        User user = ModelUtils.getUser();

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepo.findLastActivityTimeById(anyLong())).thenReturn(Optional.of(userLastActivityTime));

        assertTrue(userService.checkIfTheUserIsOnline(1L));
    }

    @Test
    void checkIfTheUserIsOnlineEqualsFalseTest() {
        ReflectionTestUtils.setField(userService, "timeAfterLastActivity", 300000);
        LocalDateTime localDateTime = LocalDateTime.of(
            2015, Month.JULY, 29, 19, 30, 40);
        Timestamp userLastActivityTime = Timestamp.valueOf(localDateTime);
        User user = ModelUtils.getUser();

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepo.findLastActivityTimeById(anyLong())).thenReturn(Optional.empty());

        assertFalse(userService.checkIfTheUserIsOnline(1L));
    }

    @Test
    void getSixFriendsWithTheHighestRatingTest() {
        List<User> friendsList = ModelUtils.getFriendsList();
        User user = User.builder()
            .id(1L)
            .userFriends(friendsList)
            .build();

        userVO.setUserFriends(friendsList.stream()
            .map(friend -> modelMapper.map(friend, UserVO.class))
            .collect(Collectors.toList()));

        when(userRepo.getSixFriendsWithTheHighestRating(user.getId())).thenReturn(user.getUserFriends().stream()
            .sorted((f1, f2) -> f2.getRating().compareTo(f1.getRating()))
            .limit(6)
            .collect(Collectors.toList()));

        assertEquals(userVO.getUserFriends().subList(2, 8),
            userService.getSixFriendsWithTheHighestRating(user.getId()));
    }

    @Test
    public void updateRoleTest() {
        UserRoleDto userRoleDto = new UserRoleDto();
        userRoleDto.setRole(Role.ROLE_ADMIN);
        userRoleDto.setId(12L);
        doNothing().when(userRepo).updateUserRole(anyLong(), anyString());
        when(userRepo.findById(any())).thenReturn(Optional.of(ModelUtils.getUser()));
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(ModelUtils.getUser()));
        when(modelMapper.map(any(), any())).thenAnswer(new Answer() {
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                if (count == 0) {
                    count++;
                    return ModelUtils.getUserVO();
                }
                if (count == 1) {
                    count++;
                    return ModelUtils.getUserVO();
                }
                return userRoleDto;
            }
        });
        assertEquals(userService.updateRole(12L, Role.ROLE_ADMIN, "email"), userRoleDto);
    }

    @Test
    public void checkUpdatableUserTest() {
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(ModelUtils.getUser()));
        when(modelMapper.map(any(), any())).thenReturn(userVO);
        Exception exception = assertThrows(BadUpdateRequestException.class, () -> {
            userService.checkUpdatableUser(1L, "email");
        });
        assertEquals(exception.getMessage(), ErrorMessage.USER_CANT_UPDATE_HIMSELF);
    }

    @Test
    public void getInitialsByIdTest() {
        when(userRepo.findById(any())).thenReturn(Optional.of(ModelUtils.getUser()));
        when(modelMapper.map(any(), any())).thenReturn(userVO);
        assertEquals("TT", userService.getInitialsById(12L));
        userVO.setName("Taras");
        assertEquals("T", userService.getInitialsById(12L));
    }
}