package com.kakaocorp.iamguide.service;

import com.kakaocorp.iamguide.IamUtils;
import com.kakaocorp.iamguide.dao.GuideUpdateMapper;
import com.kakaocorp.iamguide.model.GuideUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GuideUpdateService {

    private final GuideUpdateMapper guideUpdateMapper;

    public GuideUpdateService(GuideUpdateMapper guideUpdateMapper) {
        this.guideUpdateMapper = guideUpdateMapper;
    }


    public String retrieveGuideUpdate(String DOCUMENT_KEY) {

        GuideUpdate temp = guideUpdateMapper.retrieveGuideUpdate(DOCUMENT_KEY);
        String retrieveUpdate = temp.getAdmin() + " " + temp.getUpdateType() + " at "
                + IamUtils.timeConverting(temp.getUPDATE_TIME());
        return retrieveUpdate;
    }

    public void createGuideUpdate(GuideUpdate guideUpdate) {
        String documentKey = guideUpdate.getDocumentKey();
        if (documentKey == null || documentKey.startsWith("DIR")) {
            guideUpdate.setDocumentKey(null);
            guideUpdateMapper.createGuideUpdate(guideUpdate);
        } else if (documentKey.startsWith("DOC")) {
            guideUpdate.setDocumentKey(documentKey.substring(3));
            guideUpdateMapper.createGuideUpdate(guideUpdate);
        } else {
            guideUpdateMapper.createGuideUpdate(guideUpdate);
        }
    }
}


