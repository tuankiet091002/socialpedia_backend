package com.java.java_proj;

import com.java.java_proj.dto.response.forlist.LResponseUserPermission;
import com.java.java_proj.entities.UserPermission;
import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.repositories.UserPermissionRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
@OpenAPIDefinition(
        info =@Info(
                title = "Socialpedia API",
                version = "1.0",
                contact = @Contact(
                        name = "Tuan Kiet", email = "tuankiet091002@gmail.com", url = "http://github.com/tuankiet091002"
                ),
                license = @License(
                        name = "Term of Service", url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
                ),
                termsOfService = "${tos.uri}",
                description = "Danh sach API cua backend"
        ),
        servers = @Server(
                url = "localhost:80",
                description = "Production"
        )
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class JavaProjApplication implements CommandLineRunner {

    private final UserPermissionRepository userPermissionRepository;

    @Autowired
    public JavaProjApplication(UserPermissionRepository userPermissionRepository) {
        this.userPermissionRepository = userPermissionRepository;
    }


    public static void main(String[] args) throws IOException {
        SpringApplication.run(JavaProjApplication.class, args);
    }

    @Override
    public void run(String... args) {
        this.autoUserPermission();
    }

//    private void autoEmailContent() {
//        List<Email> email = emailRepository.findAll();
//
//        // check if email's exist
//        if (email.isEmpty()) {
//            Email emailAdd = Email.builder().subject("[FAMS] - Tài khoản được tạo thành công")
//                    .body("<p>Hi, %s, </p>" +
//                            "<p>Tài khoản đăng nhập vào hệ thống FAMS của bạn đã được tạo thành công. </p>" +
//                            "<p>Vui lòng truy cập hệ thống theo thông tin sau: </p>" +
//                            "<ul>" +
//                            "<li>Username: %s</li>" +
//                            "<li>Password: %s</li></ul>" +
//                            "<p><b>Lưu ý</b>: Vui lòng thay đổi mật khẩu sau khi đăng nhập.</p>")
//                    .build();
//
//            emailRepository.save(emailAdd);
//            System.out.println("Custom email service content added!");
//        }
//
//
//    }

    private void autoUserPermission() {
        List<LResponseUserPermission> userPermissionList = userPermissionRepository.findAllBy();

        // check if user permission's exist
        if (userPermissionList.isEmpty()) {
            UserPermission admin = UserPermission.builder()
                    .name("admin")
                    .userPermission(PermissionAccessType.MODIFY)
                    .channelPermission(PermissionAccessType.MODIFY)
                    .build();
            // user
            UserPermission user = UserPermission.builder()
                    .name("user")
                    .userPermission(PermissionAccessType.SELF)
                    .channelPermission(PermissionAccessType.SELF)
                    .build();
            // adding to db
            userPermissionRepository.save(admin);
            userPermissionRepository.save(user);

            System.out.println("Adding default user permissions settings!");
        }
    }
}
