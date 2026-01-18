package Tennis_ERP.TennisErp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {
    
    private final Path staticUploadsPath;
    private final Path targetUploadsPath;
    
    public FileUploadService() {
        // Rutas relativas al proyecto
        this.staticUploadsPath = Paths.get("src/main/resources/static/uploads/").toAbsolutePath().normalize();
        this.targetUploadsPath = Paths.get("target/classes/static/uploads/").toAbsolutePath().normalize();
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
        try {
            // Crear directorios si no existen
            Files.createDirectories(staticUploadsPath);
            Files.createDirectories(targetUploadsPath);
        } catch (IOException e) {
            throw new RuntimeException("No se pudieron crear los directorios de uploads", e);
        }
    }
    
    public String storeFile(MultipartFile file, Long userId) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        
        // Validar extensión
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        
        if (!isValidImageExtension(extension)) {
            throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, PNG o GIF.");
        }
        
        // Generar nombre único
        String uniqueFilename = userId + "_" + UUID.randomUUID() + extension;
        
        // Guardar en ambas ubicaciones
        saveToLocation(file, staticUploadsPath.resolve(uniqueFilename));
        saveToLocation(file, targetUploadsPath.resolve(uniqueFilename));
        
        return uniqueFilename;
    }
    
    public void deleteFile(String filename) throws IOException {
        if (filename == null || filename.isEmpty()) {
            return;
        }
        
        // Eliminar de ambas ubicaciones
        Path staticFile = staticUploadsPath.resolve(filename);
        Path targetFile = targetUploadsPath.resolve(filename);
        
        Files.deleteIfExists(staticFile);
        Files.deleteIfExists(targetFile);
    }
    
    private void saveToLocation(MultipartFile file, Path location) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, location, StandardCopyOption.REPLACE_EXISTING);
        }
    }
    
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
    
    private boolean isValidImageExtension(String extension) {
        String ext = extension.toLowerCase();
        return ext.equals(".jpg") || ext.equals(".jpeg") || 
               ext.equals(".png") || ext.equals(".gif") || ext.equals(".webp");
    }
    
    public Path getStaticUploadsPath() {
        return staticUploadsPath;
    }
}