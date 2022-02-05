package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.DTO.Response.FileResponse;
import com.Adam.Lucja.JavaPRO.Entity.File;
import com.Adam.Lucja.JavaPRO.Repository.FileRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * Klasa serwisowa do obsługi plików ({@link File})
 */
@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    /**
     * Metoda zapisująca przekazany plik.
     * Tworzy nowy obiekt klasy {@link File} na podstawie przekazanego pliku,
     * po czym wywołuje zapis utworzonego obiektu, za pomocą {@link FileRepository}
     * @param plik Plik przekazany w postaci {@link MultipartFile}
     * @return {@link File}
     * @throws IOException
     */
    public File saveFile(MultipartFile plik) throws IOException{
        String fileName = StringUtils.cleanPath(plik.getOriginalFilename());
        File file = new File(fileName, plik.getContentType(), plik.getBytes());
        return fileRepository.save(file);
    }

    /**
     * Metoda wywołująca metodę findById w {@link FileRepository}.
     * @param id Id ({@link String}) szukanego pliku
     * @return {@link File}
     */
    public File getFile(String id){
        return fileRepository.findById(id).get();
    }

    /**
     * Metoda wywołująca funkcję findAll na {@link FileRepository}.
     * @return  {@link Stream<File>}
     */
    public Stream<File> getAllFiles(){
        return fileRepository.findAll().stream();
    }

    /**
     * Metoda aktualizująca plik w bazie danych. Za pomocą {@link FileRepository}
     * uzyskuje obiekt {@link File} odpowiedniego pliku z bazy danych
     * modyfikuje jego atrybuty i ponownie zapisuje w bazie używając {@link FileRepository}
     * @param plik Plik w postaci {@link MultipartFile}
     * @param id Id pliku w bazie, typu {@link String}
     * @return {@link File}
     */
    @SneakyThrows //adnotacja lomboka pozwalająca na pominięcie obsługi rzucanych wyjątków
    @Transactional //adnotacja zmieniająca tryb komunikacji z bazą danych, dzięki czemu można wybierać duże dane (np. pliki z bazy)
    public FileResponse updateFile (MultipartFile plik, String id){
        File file = fileRepository.getById(id);
        file.setName(plik.getOriginalFilename());
        file.setType(plik.getContentType());
        file.setData(plik.getBytes());

        File savedFile = fileRepository.save(file);
        return new FileResponse(savedFile);
    }

    /**
     * Metoda usuwająca plik o podanym Id. Odszukuje odpowiedni plik
     * używając {@link FileRepository} i wywołuje na nim metodę delete.
     * @param id Id pliku typu {@link String}
     */
    public void deleteFile(String id){
        File file = fileRepository.getById(id);
        fileRepository.delete(file);
    }
}
