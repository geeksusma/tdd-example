package es.geeksusma.tddexample.user;

import java.util.Optional;

class GetUserDAO {
    private final UserRepository userRepository;
    private final UserDomainMapper userDomainMapper;

    GetUserDAO(final UserRepository userRepository, final UserDomainMapper userDomainMapper) {

        this.userRepository = userRepository;
        this.userDomainMapper = userDomainMapper;
    }

    Optional<User> byId(final Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id to fetch is mandatory");
        }
        return userRepository.findById(id).map( u -> userDomainMapper.map(u));
    }
}
