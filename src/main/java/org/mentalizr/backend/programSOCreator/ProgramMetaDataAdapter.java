package org.mentalizr.backend.programSOCreator;

import org.mentalizr.contentManager.fileHierarchy.levels.contentRoot.ProgramConf;
import org.mentalizr.serviceObjects.frontend.program.ProgramMetaDataSO;

public class ProgramMetaDataAdapter {

    public static ProgramMetaDataSO getProgramMetaDataSO(String programId, ProgramConf programConf) {
        ProgramMetaDataSO programMetaDataSO = new ProgramMetaDataSO();
        programMetaDataSO.setId(programId);
        programMetaDataSO.setName(programConf.getName());
        programMetaDataSO.setFamily(programConf.getFamily());
        programMetaDataSO.setTitle(programConf.getTitle());
        programMetaDataSO.setSubtitle(programConf.getSubtitle());
        programMetaDataSO.setVersion(programConf.getVersion());
        programMetaDataSO.setAuthor(programConf.getAuthor());
        programMetaDataSO.setEditor(programConf.getEditor());
        programMetaDataSO.setCopyright(programConf.getEditor());
        programMetaDataSO.setLicense(programConf.getLicense());
        programMetaDataSO.setLogo(programConf.getLogo());
        return programMetaDataSO;
    }

}
