package com.sms.data.csm.mapper;


import com.sms.data.csm.model.CsSmsRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CsSmsRecordMapper {
    int insertCsSmsRecord(@Param("csSmsRecord") CsSmsRecord csSmsRecord);
}
