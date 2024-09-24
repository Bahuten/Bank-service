package project.utility;

public class CommonUtils {
    public static final String ACCOUNT_ENDPOINT = "/account";
    public static final String ACCOUNT_ID_ENDPOINT = ACCOUNT_ENDPOINT + "/{currentAccountId}";
    public static final String ACCOUNTS_ENDPOINT = "/accounts";
    public static final String TRANSFER_ENDPOINT = "/transfer";
    public static final String TRANSFER_ID_ENDPOINT = TRANSFER_ENDPOINT + "/{targetAccountId}";
    public static final String TRANSFER_TARGET_ENDPOINT = ACCOUNT_ID_ENDPOINT + TRANSFER_ID_ENDPOINT;
    public static final String DEPOSIT_ENDPOINT = "/deposit";
    public static final String WITHDRAWAL_ENDPOINT = "/withdraw";
    public static final String REFUND_ENDPOINT = "/refund";
    public static final String REFUND_ID_ENDPOINT = REFUND_ENDPOINT + "/{targetAccountId}";
    public static final String REFUND_TARGET_ENDPOINT = ACCOUNT_ID_ENDPOINT + REFUND_ID_ENDPOINT;
    public static final String SORT_CODE_REGEX_STRING = "[0-9]{2}-[0-9]{2}-[0-9]{2}";
    public static final String ACCOUNT_NUMBER_REGEX_STRING = "[0-9]{8}";
    public static final String PASS_CODE_NUMBER_REGEX_STRING = "[0-9]{6}";
    public static final String CUSTOMER_NUMBER_REGEX_STRING = "[0-9]{11}";
    public static final String GENERATE_VERIFICATION_CODE_STRING = "[0-9]{4}";
    public static final String SORT_CODE = "sortCode";
    public static final String ACCOUNT_NUMBER = "accountNumber";
    public static final String DEPOSIT_DEFAULT_REF = "deposit";
    public static final String WITHDRAW_DEFAULT_REF = "withdraw";
    public static final String CODES_ENDPOINT = "/codes";
    public static final String PAIR_ENDPOINT = "/pair";
    public static final String LATEST_ENDPOINT = "/latest";
    public static final String RESULT = "result";
    public static final String SUPPORTED_CODE = "supported_codes";
    public static final String CONVERSION_RATES = "conversion_rates";
    public static final String CONVERSION_RESULT = "conversion_result";
    public static final String LOGIN_ENDPOINT = "/login";
    public static final String VERIFY_LOGIN_ENDPOINT = LOGIN_ENDPOINT + "/verify" + "/{currentAccountId}" + "/{verificationCode}";
    public static final String LOGOUT_ENDPOINT = "/logout" + "/{currentAccountId}";
}
