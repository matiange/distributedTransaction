package com.enjoy.service.impl;

import com.enjoy.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class XATransferServiceServiceImpl implements TransferService{
    @Autowired
    @Qualifier("dbsOneJdbcTemplate")
    private JdbcTemplate dbsOneJdbcTemplate;

    @Autowired
    @Qualifier("dbsTowJdbcTemplate")
    private JdbcTemplate dbsTowJdbcTemplate;

    @Transactional
    public String transfer(int money) {
        int resultJames = dbsOneJdbcTemplate.update("INSERT INTO bank_a(money,user_name)VALUES (?,?)",-money,"james");
        int resultPeter = dbsTowJdbcTemplate.update("INSERT INTO bank_b(money,user_name)VALUES (?,?)",money,"peter");
        if (money > 20){
            throw new RuntimeException("money too large");//系统宕机了怎么办？
        }
        return resultJames+"-"+resultPeter;
    }

}
