package com.freelycar.saas.project.service;

import com.freelycar.saas.project.entity.Partner;
import com.freelycar.saas.project.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author tangwei - Toby
 * @date 2019-08-07
 * @email toby911115@gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PartnerService {

    @Autowired
    private PartnerRepository partnerRepository;

    public Partner save(Partner partner) {
        return partnerRepository.save(partner);
    }
}
