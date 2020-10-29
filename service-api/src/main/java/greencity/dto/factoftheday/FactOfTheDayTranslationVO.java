package greencity.dto.factoftheday;

import greencity.dto.translation.TranslationVO;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class FactOfTheDayTranslationVO extends TranslationVO {
    private FactOfTheDayVO factOfTheDay;
}
