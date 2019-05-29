package com.sms.data.csm.mapper;


import com.sms.data.csm.model.CsTemplateCode;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CsTemplateCodeMapper {
    List<CsTemplateCode> selectCsTemplateCodeAll();
    CsTemplateCode selectCsTemplateCode(@Param("templateCode")String templateCode);
}
