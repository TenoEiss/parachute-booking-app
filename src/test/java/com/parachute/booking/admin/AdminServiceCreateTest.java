package com.parachute.booking.admin;

import com.parachute.booking.exceptions.BlankSpaceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AdminServiceCreateTest {

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    AdminServiceCreate adminServiceCreate;
    @Autowired
    AdminMapper adminMapper;
    @Autowired
    AdminDataValidate adminDataValidate;

    @BeforeEach
    void setup() {
        adminRepository.deleteAll();
    }

    @Test
    void createAdmin_passwordIsBlank() {
        AdminDto adminDto = new AdminDto.AdminDtoBuilder()
                .login("   ")
                .password("Admin pass")
                .email("admin@gmail.com")
                .build();

        Throwable result = catchThrowable(() -> adminServiceCreate.createNewAdmin(adminDto));

        assertThat(result).isExactlyInstanceOf(BlankSpaceException.class);
    }

    @Test
    void createAdmin_emailIsBlank() {
        AdminDto adminDto = new AdminDto.AdminDtoBuilder()
                .login("Admin1")
                .password("Admin pass")
                .email("  ")
                .build();

        Throwable result = catchThrowable(() -> adminServiceCreate.createNewAdmin(adminDto));

        assertThat(result).isExactlyInstanceOf(BlankSpaceException.class);
    }

    @Test
    void createAdmin_loginIsBlank() {
        AdminDto adminDto = new AdminDto.AdminDtoBuilder()
                .login("Admin1")
                .password("   ")
                .email("admin@gmail.com")
                .build();

        Throwable result = catchThrowable(() -> adminServiceCreate.createNewAdmin(adminDto));

        assertThat(result).isExactlyInstanceOf(BlankSpaceException.class);
    }

    @Test
    void createAdmin_DtoIsNull() {
        AdminDto adminDto = new AdminDto(null, null, null, null);

        Throwable result = catchThrowable(() -> adminServiceCreate.createNewAdmin(adminDto));

        assertThat(result).isExactlyInstanceOf(NullPointerException.class);

    }

    //    @Test
//    void createAdmin_saveAdminToRepository() {
//        //given
//        when(adminRepository.save(any(Admin.class))).thenReturn(new Admin());
//
//        //when
//        AdminDto newAdminDto = adminServiceCreate.createNewAdmin(new AdminDto(1L, "Admin1", "Admin pass", "admin@gmail.com"));
//
//        //then
//        assertThat(newAdminDto).isExactlyInstanceOf(AdminDto.class);
//        verify(adminRepository).save(any(Admin.class));
//    }

    //    @Test
//    void createAdmin_emailContainsNoMonkey() {
//        AdminDto adminDto1 = new AdminDto.AdminDtoBuilder()
//                .login("Admin1")
//                .password("Admin pass")
//                .email("admin(at)gmail.com")
//                .build();
//
//        Throwable result = catchThrowable(() -> adminServiceCreate.createNewAdmin(adminDto1));
//
//        assertThat(result).isExactlyInstanceOf(BadRequestException.class);
//    }
//

}
