package greencity.webcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import greencity.ModelUtils;
import greencity.client.RestClient;
import greencity.converters.UserArgumentResolver;
import greencity.dto.PageableAdvancedDto;
import greencity.dto.user.*;
import greencity.entity.UserShoppingListItem;
import greencity.enums.Role;
import greencity.enums.ShoppingListItemStatus;
import greencity.service.HabitAssignService;
import greencity.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static greencity.ModelUtils.getUserShoppingListItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ManagementUserControllerTest {
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RestClient restClient;
    @InjectMocks
    private ManagementUserController managementUserController;
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    private static final String managementUserLink = "/management/users";
    @Mock
    private UserService userService;
    @Mock
    HabitAssignService habitAssignService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(managementUserController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(),
                new UserArgumentResolver(userService, modelMapper))
            .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void changeRoleTest() {
        UserVO userVO = new UserVO();
        userVO.setEmail("test@mail.com");
        managementUserController.changeRole(5L, "ROLE_ADMIN", userVO);
        verify(userService, times(1)).updateRole(5L, Role.ROLE_ADMIN, userVO.getEmail());
    }

    @Test
    void getAllUsers() throws Exception {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("id").descending());
        List<UserManagementVO> userManagementVO = Collections.singletonList(new UserManagementVO());
        PageableAdvancedDto<UserManagementVO> userAdvancedDto =
            new PageableAdvancedDto<>(userManagementVO, 20, 0, 0, 0,
                true, true, true, true);
        UserManagementViewDto userManagementViewDto = new UserManagementViewDto();
        when(restClient.search(pageable, userManagementViewDto)).thenReturn(userAdvancedDto);
        mockMvc.perform(get(managementUserLink +
            "?page=" + 0 + "&size=" + 20 + "&sort=id,DESC")).andExpect(model()
                .attribute("users", userAdvancedDto));
        verify(restClient).search(pageable, userManagementViewDto);
    }

    @Test
    void searchTest() throws Exception {
        Pageable pageable = PageRequest.of(0, 20, Sort.unsorted());
        UserManagementViewDto userViewDto =
            UserManagementViewDto.builder()
                .id("1L")
                .name("vivo")
                .email("test@ukr.net")
                .userCredo("Hello")
                .role("1")
                .userStatus("1")
                .build();
        String content = objectMapper.writeValueAsString(userViewDto);
        List<UserManagementVO> userManagementVOS = Collections.singletonList(new UserManagementVO());
        PageableAdvancedDto<UserManagementVO> userAdvancedDto =
            new PageableAdvancedDto<>(userManagementVOS, 20, 0, 0, 0,
                true, true, true, true);
        when(restClient.search(pageable, userViewDto)).thenReturn(userAdvancedDto);
        mockMvc.perform(post(managementUserLink + "/search")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void getReasonsOfDeactivation() throws Exception {
        List<String> test = List.of("test", "test");
        when(restClient.getDeactivationReason(1L, "en")).thenReturn(test);
        this.mockMvc.perform(get(managementUserLink + "/reasons" + "?id=1" + "&admin=en")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        verify(restClient).getDeactivationReason(1L, "en");
    }

    @Test
    void setActivatedStatus() throws Exception {
        mockMvc.perform(post(managementUserLink + "/activate" + "?id=1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        verify(restClient).setActivatedStatus(1L);
    }

    @Test
    void getUserLang() throws Exception {
        this.mockMvc.perform(get(managementUserLink + "/lang" + "?id=1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        verify(restClient).getUserLang(1L);
    }

    @Test
    void deactivateUser() throws Exception {
        List<String> test = List.of("test", "test");
        String json = objectMapper.writeValueAsString(test);
        mockMvc.perform(post(managementUserLink + "/deactivate" + "?id=1")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        verify(restClient).deactivateUser(1L, test);
    }

    @Test
    void saveUserTest() throws Exception {

        UserManagementDto userManagementDto = ModelUtils.getUserManagementDto();
        String content = objectMapper.writeValueAsString(userManagementDto);

        mockMvc.perform(post(managementUserLink + "/register")
            .content(content)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(restClient).managementRegisterUser(userManagementDto);
    }

    @Test
    void updateUserTest() throws Exception {
        UserManagementDto userManagementDto = ModelUtils.getUserManagementDto();
        String context = objectMapper.writeValueAsString(userManagementDto);

        mockMvc.perform(put(managementUserLink)
            .content(context)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(restClient).updateUser(userManagementDto);
    }

    @Test
    void getUserById() throws Exception {
        mockMvc.perform(get(managementUserLink + "/findById" + "?id=1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        verify(restClient).findById(1L);
    }

    @Test
    void findFriendsByIdTest() throws Exception {
        mockMvc.perform(get(managementUserLink + "/" + 1L + "/friends")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        verify(restClient).findUserFriendsByUserId(1L);
    }

    @Test
    void deactivateAllTest() throws Exception {
        List<Long> list = List.of(1L, 2L);
        String context = objectMapper.writeValueAsString(list);
        mockMvc.perform(post(managementUserLink + "/deactivateAll")
            .content(context)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        context = objectMapper.writeValueAsString(null);

        mockMvc.perform(post(managementUserLink + "/deactivateAll")
            .content(context)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(restClient).deactivateAllUsers(list);
    }

    @Test
    void updateUserRole() throws Exception {
        mockMvc.perform(put(managementUserLink + "/updateShoppingItem/" + 1L + "/" + 1L)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        verify(habitAssignService).updateShoppingItem(1L, 1L);
    }
}
