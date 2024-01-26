package com.dailystudy.dtmsapi.mapper;

import com.dailystudy.dtmsapi.base.DbMap;

public interface EmployeeMapper {

    DbMap userIdentifier(DbMap map);

    DbMap employee(DbMap map);

    int memberExtraPrivilegeCount(DbMap map);

    int accessibleAcademy(DbMap map);

    DbMap accessibleMenu(DbMap map);

}
