package kr.hs.dgsw.camfit.photo.service;

import kr.hs.dgsw.camfit.photo.Photo;
import kr.hs.dgsw.camfit.photo.dto.PhotoListDTO;
import kr.hs.dgsw.camfit.photo.dto.PhotoResponseDTO;

import java.util.List;

public interface PhotoService {
    public PhotoListDTO findByFileId(Long id);
    public List<PhotoResponseDTO> findAllByBoard(Long boardId);
    public List<String> findByIdList(Long id);
}
