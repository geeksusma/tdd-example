package es.geeksusma.tddexample.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class GetUserDAOTest {

    private GetUserDAO getUserDAO;
    private UserRepository userRepository;
    private UserDomainMapper userDomainMapper;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userDomainMapper = Mockito.mock(UserDomainMapper.class);
        getUserDAO = new GetUserDAO(userRepository, userDomainMapper);
    }

    @Test
    void should_throwIllegalUse_when_idNotGiven() {

        //when
        final Throwable raisedException = catchThrowable(() -> getUserDAO.byId(null));

        //then
        assertThat(raisedException).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Id to fetch is mandatory");
    }

    @Test
    void should_returnEmpty_when_notFound() {
        //given
        final Long notExistingId = 123L;

        //when
        final Optional<User> found = getUserDAO.byId(notExistingId);

        //then
        assertThat(found).isEmpty();
    }

    @Test
    void should_fetchFromRepository_when_userIdIsValid() {
        //given
        final Long userId = 321L;

        //when
        getUserDAO.byId(userId);

        //then
        then(userRepository).should().findById(userId);
    }

    @Test
    void should_mapEntity_when_found() {
        //given
        final Long existingUserId = 67676L;
        final UserEntity foundUser = new UserEntity();

        given(userRepository.findById(existingUserId)).willReturn(Optional.of(foundUser));

        //when
        getUserDAO.byId(existingUserId);

        //then
        then(userDomainMapper).should().map(foundUser);
    }

    @Test
    void should_returnUser_when_found() {
        //given
        final Long existingUserId = 77777L;
        final UserEntity foundUser = new UserEntity();
        final User expectedUser = new User();

        given(userRepository.findById(existingUserId)).willReturn(Optional.of(foundUser));
        given(userDomainMapper.map(foundUser)).willReturn(expectedUser);

        //when
        final Optional<User> fetchedUser = getUserDAO.byId(existingUserId);

        //then
        assertThat(fetchedUser.get()).isEqualTo(expectedUser);
    }
}
