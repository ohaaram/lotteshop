package kr.co.lotte.service;

import kr.co.lotte.dto.PolicyDTO;
import kr.co.lotte.entity.Policy;
import kr.co.lotte.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;

    public Policy buyerPolicy(Long id) {
        Optional<Policy> policy = policyRepository.findById(id);
        return policy.get();
    }

    @Transactional
    public void policyModify(Long id, PolicyDTO policyDTO) {
        Policy policy = buyerPolicy(id);
        policy.setChapter1(policyDTO.getChapter1());
        policy.setChapter2(policyDTO.getChapter2());
        policy.setChapter3(policyDTO.getChapter3());
        policy.setChapter4(policyDTO.getChapter4());
        policy.setChapter5(policyDTO.getChapter5());
        policy.setChapter6(policyDTO.getChapter6());
    }

}

