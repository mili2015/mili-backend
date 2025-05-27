package br.com.mili.milibackend.shared.infra.security.service;

import br.com.mili.milibackend.shared.infra.security.model.Roles;
import br.com.mili.milibackend.shared.infra.security.repository.AuthRepository;
import br.com.mili.milibackend.shared.infra.security.repository.projections.SystemAuthProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthRepository repository;

    public List<Roles> getRolesByUserId(Integer userId) {
        List<SystemAuthProjection> rawData = repository.findUserAuthorities(userId);

        Map<Integer, List<String>> grouped = rawData.stream()
                .collect(Collectors.groupingBy(
                        SystemAuthProjection::getSystemId,
                        Collectors.mapping(SystemAuthProjection::getAuthority, Collectors.toList())
                ));

        return grouped.entrySet().stream()
                .map(entry -> new Roles(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
