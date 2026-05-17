package com.psw.management.service;

import com.psw.management.entity.Directory;

import java.util.List;

public interface DirectoryService extends BaseService<Directory>{
    List<Directory> getByUserId(Long userId);

}
