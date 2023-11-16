package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.*;
import com.sparta.myselectshop.entity.*;
import com.sparta.myselectshop.repository.*;
import java.util.*;
import lombok.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class FolderService {
private final FolderRepository folderRepository;
    public void addFolders(List<String> folderNames, User user) {
        List<Folder> existFolderList = folderRepository.findAllByUserAndNameIn(user, folderNames);

        List<Folder> folderList = new ArrayList<>();

        for (String folderName : folderNames) {
            if (!isExistFolderName(folderName, existFolderList)){
                Folder folder = new Folder(folderName, user);
                folderList.add(folder);
            } else {
                throw new IllegalArgumentException("폴더명이 중복이잖아요!");
            }
        }
        folderRepository.saveAll(folderList);
    }

    public List<FolderResponseDto> getFolders(User user) {
        List<Folder> folderList = folderRepository.findAllByUser(user);
        List<FolderResponseDto> responseDtoList = new ArrayList<>();

        for (Folder folder : folderList) {
            responseDtoList.add(new FolderResponseDto(folder));
        }
        return responseDtoList;
    }
    private boolean isExistFolderName(String folderName, List<Folder> existFolderList) {
        for (Folder exsistFolder : existFolderList) {
            if(folderName.equals(exsistFolder.getName())) {
                return true;
            }
        }
        return false;
    }

}
