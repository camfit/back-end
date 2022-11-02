package kr.hs.dgsw.camfit.photo.service;

import kr.hs.dgsw.camfit.board.Board;
import kr.hs.dgsw.camfit.exception.FileFailedException;
import kr.hs.dgsw.camfit.photo.Photo;
import kr.hs.dgsw.camfit.photo.dto.PhotoInsertDTO;
import kr.hs.dgsw.camfit.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileHandler {

    private final PhotoRepository photoRepository;

    public List<Photo> parseFileInfo(Board board, List<MultipartFile> multipartFiles) throws IOException {

        // 변환할 파일 리스트
        List<Photo> fileList = new ArrayList<>();

        // 전달되어 온 파일이 존재할 경우
        if(!CollectionUtils.isEmpty(multipartFiles)) {
            System.out.println("============================== fileHandler");
            // 파일명을 UUID로 저장
            UUID uuid = UUID.randomUUID();

            // 프로젝트 디렉터리 내의 저장을 위한 절대 경로 설정
            // 경로 구분자 File.separator 사용
            String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;

            // 파일을 저장할 세부 경로 지정
            //"src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "images"
            String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "images" + File.separator + uuid;
            File file = new File(path);

            // 디렉터리가 존재하지 않을 경우
            if(!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                // 디렉터리 생성에 실패했을 경우
                if(!wasSuccessful) {
                    throw new FileFailedException("파일 생성 실패");
                }
            }

            // 다중 파일 처리
            for (MultipartFile multipartFile : multipartFiles) {

                // 파일 확장자 추출
                String originalFileExtension;
                String contentType = multipartFile.getContentType();

                //확장명이 존재하지 않을 경우 X
                if(ObjectUtils.isEmpty(contentType)) {
                    System.out.println("확장명이 없음");
                    break;
                } else { // 확장자가 jpeg, png인 파일들만 받아서 처리
                    if(contentType.equals("image/jpeg")) {
                        originalFileExtension = ".jpg";
                    } else if(contentType.equals("image/png")) {
                        originalFileExtension = ".png";
                    } else { //다른 확장자인 경우 처리 X
                        System.out.println("다른 확장자");
                        break;
                    }
                }

                // 파일명 중복을 피하기 위해 나노초까지 얻어와 지정
                String newFileName = System.nanoTime() + originalFileExtension;

                // 파일 DTO 생성
                PhotoInsertDTO photoInsertDTO = PhotoInsertDTO.builder()
                        .origFileName(multipartFile.getOriginalFilename())
                        .filePath(path + File.separator + newFileName)
                        .fileSize(multipartFile.getSize())
                        .build();

                // 파일 DTO를 사용하여 Photo 앤티티 생성
                Photo photo = Photo.builder()
                        .origFileName(photoInsertDTO.getOrigFileName())
                        .filePath(photoInsertDTO.getFilePath())
                        .fileSize(photoInsertDTO.getFileSize())
                        .build();

                // 게시글에 존재 X -> 게시글에 사진 정보 저장
                if(board.getId() != null) {
                    board.addPhoto(photo);
                }

                // 생성 후 리스트에 추가
                fileList.add(photo);

                System.out.println("===================== 파일 저장 전");
                // 업로드 한 파일 데이터를 지정한 파일에 저장
                file = new File(absolutePath + path + File.separator + newFileName);
                multipartFile.transferTo(file);

                // 파일 권한 설정(쓰기, 읽기)
                file.setWritable(true);
                file.setReadable(true);
            }
        }

        return fileList;
    }
}
