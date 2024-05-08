package kr.co.lotte.service;

import kr.co.lotte.entity.Policy;
import kr.co.lotte.repository.PolicyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PolicyServiceTest {

    @Autowired
    PolicyRepository policyRepository;

    @Autowired
    PolicyService policyService;

    @Test

    public void whyNull() {

        Optional<Policy> policy = policyRepository.findById(1L);
        assertThat(policy.get().getChapter1()).isNotEmpty();
    }


}