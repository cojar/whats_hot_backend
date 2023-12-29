package com.cojar.whats_hot_backend.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
public enum ResCode {

    // index controller success codes
    S_00_00(HttpStatus.OK, "S-00-00", "인덱스 링크 목록을 반환합니다"),

    // member controller success codes
    S_01_01(HttpStatus.CREATED, "S-01-01", "회원가입을 완료했습니다"),
    S_01_02(HttpStatus.OK, "S-01-02", "액세스 토큰이 생성되었습니다"),
    S_01_03(HttpStatus.OK, "S-01-03", "로그아웃이 완료되었습니다"),
    S_01_04(HttpStatus.OK, "S-01-04", "로그인된 회원 정보를 반환합니다"),
    S_01_05(HttpStatus.OK, "S-01-05", "비밀번호 변경을 완료했습니다"),
    S_01_06(HttpStatus.OK, "S-01-06", "요청하신 아이디를 반환합니다"),
    S_01_07(HttpStatus.OK, "S-01-07", "이메일로 임시비밀번호를 발송했습니다"),

    // spot controller success codes
    S_02_01(HttpStatus.CREATED, "S-02-01", "장소 등록이 완료되었습니다"),
    S_02_02(HttpStatus.OK, "S-02-02", "요청하신 장소 목록을 반환합니다"),
    S_02_03(HttpStatus.OK, "S-02-03", "요청하신 장소 정보를 반환합니다"),
    S_02_04(HttpStatus.OK, "S-02-04", "장소 수정이 완료되었습니다"),
    S_02_05(HttpStatus.OK, "S-02-05", "장소 삭제가 완료되었습니다"),

    // review controller success codes
    S_03_01(HttpStatus.CREATED, "S-03-01", "리뷰 등록이 완료되었습니다"),
    S_03_02(HttpStatus.OK, "S-03-02", "요청하신 리뷰 목록을 반환합니다"),
    S_03_03(HttpStatus.OK, "S-03-03", "요청하신 리뷰 정보를 반환합니다"),
    S_03_04(HttpStatus.OK, "S-03-04", "리뷰 수정이 완료되었습니다"),
    S_03_05(HttpStatus.OK, "S-03-05", "리뷰 삭제가 완료되었습니다"),
    S_03_06(HttpStatus.OK, "S-03-06", "리뷰 좋아요 상태가 변경되었습니다"),

    // comment controller success codes
    S_04_01(HttpStatus.CREATED, "S-04-01", "댓글 등록이 완료되었습니다"),
    S_04_02(HttpStatus.OK, "S-04-02", "요청하신 댓글 정보를 반환합니다"),
    S_04_03(HttpStatus.OK, "S-04-03", "요청하신 댓글 목록을 반환합니다"),
    S_04_04(HttpStatus.OK, "S-04-04", "댓글 수정이 완료되었습니다"),
    S_04_05(HttpStatus.OK, "S-04-05", "댓글 삭제가 완료되었습니다"),
    S_04_06(HttpStatus.OK, "S-04-06", "댓글 좋아요 상태가 변경되었습니다"),

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
    F_02_05_01(HttpStatus.BAD_REQUEST, "F-02-05-01", "해당 아이디를 가진 장소가 존재하지 않습니다"),

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

    public static ResCode fromCode(String code) {
        return Arrays.stream(ResCode.values())
                .filter(resCode -> resCode.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
