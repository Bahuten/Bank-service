package project.utility;

import com.mifmif.common.regex.Generex;

import static project.utility.CommonUtils.*;

public class CodeGenerator {
    private static final Generex sortCodeRegex = new Generex(SORT_CODE_REGEX_STRING);
    private static final Generex accountNumberRegex = new Generex(ACCOUNT_NUMBER_REGEX_STRING);
    private static final Generex passNumberRegex = new Generex(PASS_CODE_NUMBER_REGEX_STRING);
    private static final Generex customerNumberRegex = new Generex(CUSTOMER_NUMBER_REGEX_STRING);
    private static final Generex generateVerificationCodeRegex = new Generex(GENERATE_VERIFICATION_CODE_STRING);

    public CodeGenerator() {
    }

    public static String generateSortCode() {
        return sortCodeRegex.random();
    }

    public static String generateAccountNumber() {
        return accountNumberRegex.random();
    }

    public static String generateCustomerNumber() {
        return customerNumberRegex.random();
    }

    public static String generatePassNumber() {
        return passNumberRegex.random();
    }

    public static String generateVerificationCode() {
        return generateVerificationCodeRegex.random();
    }
}
