package kr.hs.dgsw.camfit.photo.service;

import kr.hs.dgsw.camfit.board.Board;
import kr.hs.dgsw.camfit.exception.WrongIdException;
import kr.hs.dgsw.camfit.photo.Photo;
import kr.hs.dgsw.camfit.photo.dto.PhotoListDTO;
import kr.hs.dgsw.camfit.photo.dto.PhotoResponseDTO;
import kr.hs.dgsw.camfit.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    /**
     * 이미지 개별 조회
     */
    @Override
    public PhotoListDTO findByFileId(Long id) {

        Photo photo = photoRepository.findById(id).orElseThrow(() -> new WrongIdException("존재하지 않는 사진"));

        PhotoListDTO photoListDTO = PhotoListDTO.builder()
                .origFileName(photo.getOrigFileName())
                .filePath(photo.getFilePath())
                .fileSize(photo.getFileSize())
                .build();

        return photoListDTO;
    }

    /**
     * 이미지 전체 조회
     */
    @Override
    public List<PhotoResponseDTO> findAllByBoard(Long boardId) {

        List<Photo> photos = photoRepository.findByBoardId(boardId);

        return photos.stream()
                .map(PhotoResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 이미지 경로 반환
     */
    @Override
    public List<String> findByIdList(Long boardId) {

        List<String> photoPath = new ArrayList<>();
        List<Photo> photoList = photoRepository.findByBoardId(boardId);

        photoList.forEach(p -> {
            photoPath.add(p.getFilePath());
        });

        return photoPath;
    }
}
