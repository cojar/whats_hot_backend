package com.cojar.whats_hot_backend.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResCode {

    // file service fail codes
    F_00_00_01(HttpStatus.BAD_REQUEST, "F-00-00-01", "이미지 형식만 업로드할 수 있습니다"),
    F_00_00_02(HttpStatus.BAD_REQUEST, "F-00-00-02", "JPG, JPEG, PNG 확장자만 업로드할 수 있습니다"),
    F_00_00_03(HttpStatus.INTERNAL_SERVER_ERROR, "F-00-00-03", "이미지 저장 중 오류가 발생했습니다"),

    // mail service fail codes
    F_00_02_01(HttpStatus.INTERNAL_SERVER_ERROR, "F-00-02-01", "메일 발송 중 오류가 발생했습니다"),

    // member service fail codes
    F_01_01_01(HttpStatus.BAD_REQUEST, "F-01-01-01", "요청 값이 올바르지 않습니다"),
    F_01_01_02(HttpStatus.BAD_REQUEST, "F-01-01-02", "비밀번호가 서로 일치하지 않습니다"),
    F_01_01_03(HttpStatus.BAD_REQUEST, "F-01-01-03", "이미 존재하는 아이디입니다"),
    F_01_01_04(HttpStatus.BAD_REQUEST, "F-01-01-04", "이미 존재하는 이메일입니다"),
    F_01_02_01(HttpStatus.BAD_REQUEST, "F-01-02-01", "요청 값이 올바르지 않습니다"),
    F_01_02_02(HttpStatus.BAD_REQUEST, "F-01-02-02", "존재하지 않는 회원입니다"),
    F_01_02_03(HttpStatus.BAD_REQUEST, "F-01-02-03", "비밀번호가 일치하지 않습니다"),
    F_01_05_01(HttpStatus.BAD_REQUEST, "F-01-05-01", "요청 값이 올바르지 않습니다"),
    F_01_05_02(HttpStatus.BAD_REQUEST, "F-01-05-02", "기존 비밀번호가 일치하지 않습니다"),
    F_01_05_03(HttpStatus.BAD_REQUEST, "F-01-05-03", "새 비밀번호가 일치하지 않습니다"),
    F_01_06_01(HttpStatus.BAD_REQUEST, "F-01-06-01", "요청 값이 올바르지 않습니다"),
    F_01_06_02(HttpStatus.BAD_REQUEST, "F-01-06-02", "해당 이메일을 보유한 회원이 존재하지 않습니다"),
    F_01_07_01(HttpStatus.BAD_REQUEST, "F-01-07-01", "요청 값이 올바르지 않습니다"),
    F_01_07_02(HttpStatus.BAD_REQUEST, "F-01-07-02", "해당 아이디 또는 이메일을 보유한 회원이 존재하지 않습니다"),

    // spot service fail codes
    F_02_01_01(HttpStatus.BAD_REQUEST, "F-02-01-01", "요청 값이 올바르지 않습니다"),
    F_02_01_02(HttpStatus.BAD_REQUEST, "F-02-01-02", "존재하지 않는 카테고리입니다"),
    F_02_01_03(HttpStatus.BAD_REQUEST, "F-02-01-03", "소분류 카테고리 아이디를 입력해주세요"),
    F_02_01_04(HttpStatus.BAD_REQUEST, "F-02-01-04", "같은 이름과 주소를 가진 장소가 이미 존재합니다"),
    F_02_04_01(HttpStatus.BAD_REQUEST, "F-02-04-01", "해당 아이디를 가진 장소가 존재하지 않습니다"),
    F_02_04_02(HttpStatus.BAD_REQUEST, "F-02-04-02", "요청 값이 올바르지 않습니다"),
    F_02_04_03(HttpStatus.BAD_REQUEST, "F-02-04-03", "존재하지 않는 카테고리입니다"),
    F_02_04_04(HttpStatus.BAD_REQUEST, "F-02-04-04", "소분류 카테고리 아이디를 입력해주세요"),
    F_02_04_05(HttpStatus.BAD_REQUEST, "F-02-04-05", "같은 이름과 주소를 가진 장소가 이미 존재합니다"),

    // review service fail codes
    F_03_01_01(HttpStatus.BAD_REQUEST, "F-03-01-01", "요청 값이 올바르지 않습니다"),
    F_03_01_02(HttpStatus.BAD_REQUEST, "F-03-01-02", "해당 아이디를 가진 장소가 존재하지 않습니다"),

    // comment service fail codes
    F_04_01_01(HttpStatus.BAD_REQUEST, "F-04-01-01", "해당 아이디를 가진 리뷰가 존재하지 않습니다"),
    F_04_01_02(HttpStatus.BAD_REQUEST, "F-04-01-02", "요청 값이 올바르지 않습니다"),
    F_04_02_01(HttpStatus.BAD_REQUEST, "F-04-02-01", "해당 아이디를 가진 댓글이 존재하지 않습니다"),
    F_04_03_01(HttpStatus.BAD_REQUEST, "F-04-03-01", "해당 회원이 작성한 댓글이 없습니다"),
    F_04_04_01(HttpStatus.BAD_REQUEST, "F-04-04-01", "요청 값이 올바르지 않습니다"),
    F_04_04_02(HttpStatus.BAD_REQUEST, "F-04-04-02", "해당 아이디를 가진 댓글이 존재하지 않습니다"),
    F_04_04_03(HttpStatus.BAD_REQUEST, "F-04-04-03", "해당 댓글에 대한 수정 권한이 없습니다"),
    F_04_05_01(HttpStatus.BAD_REQUEST, "F-04-05-01", "해당 아이디를 가진 댓글이 존재하지 않습니다"),
    F_04_05_02(HttpStatus.BAD_REQUEST, "F-04-05-02", "해당 댓글에 대한 삭제 권한이 없습니다"),
    F_04_06_01(HttpStatus.BAD_REQUEST, "F-04-06-01", "해당 아이디를 가진 댓글이 존재하지 않습니다"),
    F_04_06_02(HttpStatus.BAD_REQUEST, "F-04-06-02", "본인이 작성한 댓글에는 좋아요를 누를 수 없습니다")
    ;

    private HttpStatus status;
    private String code;
    private String message;

    ResCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
