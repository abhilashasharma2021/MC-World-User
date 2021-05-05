package com.app.mcworlduser.FCM;

public class Const {

    public static final String SERVER_REMOTE_URL = "https://jeemkw.com/";
//        public static final String SERVER_REMOTE_URL = "http://192.168.2.121/jeem-web-949/";
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1234;

    public static final int GALLERY_REQ = 1;
    public static final int CAMERA_REQ = 2;
    public static final String ID = "id";
    public static final String DATA = "data";
    public static final String PICTURE = "picture";
    public static final String PAYMENT_ONLINE = "1";
    public static final int GOOGLE_REQUEST = 1534;
    public static final int GOOGLE_SIGN_TYPE = 1;
    public static final int FACEBOOK_SIGN_TYPE = 2;

    public static final String DEVICE_TYPE = "1";
    public static final String CANCEL_PAGE = "https://www.hesabe.com/pgcancel/";

    public static final String API_CHECK = "api/user/check";
    public static final int PLACE_PICKER_REQUEST = 1125;
    public static final int REQUEST_CODE_PLACE = 4324;
    public static final String DISPLAY_MESSAGE_ACTION = "jeem.app.DISPLAY_MESSAGE";
    public static final String FORGROUND = "forground_notification";


    public static final int STATUS_OK = 200;
    public static final String COUNTRY_CODE = "+91";
    public static final String API_USER_PAGE = "api/user/page";//?type

    public static final String API_SIGNUP = "api/user/signup";
    public static final String LOG_IN_API = "api/user/login";
    public static final String API_PROFILE_UPDATE = "api/user/update";
    public static final String API_LOGOUT = "api/user/logout";
    public static final String DEVICE_NAME = "android";
    public static final String API_RECOVERY = "api/user/recover";
    public static final String API_CATEGORY = "api/category/category-list";
    public static final String API_SUB_CATEGORY = "api/category/sub-category-list";//?id=
    public static final String API_OCCASION = "api/product/occassion";
    public static final String API_SOCIAL_LOGIN = "api/user/social-login";


    /* Guest Case */
    public static final String API_GUEST_ADD_TO_CART = "api/product/guest-add-to-cart";
    public static final String API_GUEST_UPDATE_CART = "api/product/guest-update-cart"; //?id=&device_id=
    public static final String API_GUEST_CART_LIST = "api/product/guest-cart-list";
    public static final String API_GUEST_REMOVE_ITEM = "api/product/guest-remove-item";//?id=&device_id=
    public static final String API_GUEST_ADD_ORDER = "api/product/guest-add-order";//?id=&device_id=
    public static final String API_ORDER_TRACKING = "api/order/track";//?trackId=
    public static final String API_ORDER_DETAIL = "api/product/orders-detail";//?id=

    public static final String API_GUEST_SEND_OTP = "api/user/otp";
    public static final String API_PAYMENT_LIST = "api/product/payment-options";
    public static final String API_GUEST_VERIFY_OTP = "api/user/guest-opt-verify";
    public static final String API_CHANGE_PASSWORD = "api/user/change-password";

    public static final String API_ADD_TO_CART = "api/product/add-to-cart";
    public static final String API_CART_LIST = "api/product/cart-list";

    //order APi
    public static final String API_ORDER_LIST = "api/order/list"; //?state&page=


    public static final String API_AREAS = "api/user/city";//?countryId=&page=
    public static final String API_ADD_ADDRESS = "api/user/users-delivery-address";
    public static final String API_UPDATE_CART = "api/product/update-cart-product";
    public static final String API_ADDRESS_LIST = "api/user/users-delivery-address-list";
    public static final String API_ADDRESS_DELETE = "api/user/delete-address";//?id=

    public static final String API_REMOVE_ITEM = "api/product/remove-item";//?id=
    public static final String API_PRODUCT_DETAIL = "api/product/product-detail";//?id=


    public static final String API_PAYMENT_OPTION = "api/product/payment-options";//?id=
    public static final String API_SEARCH = "api/product/search-product";
    public static final String API_UPDATE_ADDRESS = "api/user/update-users-delivery-address";
    public static final String API_RECENT_USED_ADDRESS = "api/user/recently-used-address";
    public static final String API_SEND_VERIFY_OTP_LOGIN = "api/user/verify-otp-to-login";
    public static final String API_GUEST_SIGNUP = "api/user/guest-signup";
    public static final String API_PRODUCT_LIST = "api/product/list";  //?id=$type=&lat=&long=&page=

    public static final String API_OCCASION_LIST = "api/category/occassion-based-products";
    public static final String API_COUNTRY_LIST = "api/user/country";
    public static final String API_RECOVERY_PASS = "api/user/recover-password";

    public static final String API_COUPON_LIST = "api/product/offer-list";
    public static final String API_REMOVE_COUPON = "api/product/remove-coupon";
    public static final String API_APPLY_COUPON = "api/product/apply-coupon";
    public static final String API_FILTER = "api/product/filter-search";
    public static final String API_OFFER_LIST = "api/offer/list";
    public static final String API_OFFER_ITEM = "api/offer/offer-list";
    public static final String API_MOST_POPULAR = "api/product/most-popular";
    public static final String API_NEW_ARRIVAL = "api/product/new-arrivals";
    public static final String API_FILTER_PRODUCT = "api/product/filter-product";

    public static final String STORE_PROFILE_DATA = "userdata";
    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
    public static final String MALE_STRING = "0";
    public static final String CONST_ENGLISH = "English";
    public static final String CONST_ARABIC = "عربى";
    public static final int REQUEST_CODE = 2020;

    public static final int USER = 2;
    public static final int GUEST = 4;


    public static final String ARABIC = "ar";
    public static final String LANGUAGE = "language";
    public static final String ENGLISH = "en";


    public static final String API_ADD_ORDER = "api/product/add-order";
    public static final String paymentMethod = "1";
    public static final String BADGE_COUNT = "badge";
    public static final int PERM_MAP_LOCATION = 8500;
    public static final int PERM_LOCATION = 120;

    //Order Status
    public static final int STATE_ORDER = 0;
    public static final int STATE_PACKED = 1;
    public static final int STATE_SHIPPED = 2;
    public static final int STATE_COMPLETED = 3;


    //Filter Type
    public static final int LOW_TO_HIGH = 3;
    public static final int HIGH_TO_LOW = 4;
    public static final int NEWEST = 5;
    public static final int BIG_DISCOUNT = 6;

    //Order type
    public static final int CURRENT = 0;
    public static final int HISTORY = 3;

    //page type
    public static final int TYPE_ABOUT_US = 0;
    public static final int TYPE_HELP = 1;
    public static final int TYPE_TERMS_AND_CONDITIONS = 2;
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_GIFT = 1;
    public static final int SORT_ASCENDING = 0;
    public static final int SORT_DESCENDING = 1;
    public static final int SORT_DATE = 2;
    public static final int TYPE_OCASSION = 2;
    public static final int SIZES = 1;
    public static final int COLORS = 2;
    public static final int PERMISSION_DENIED = 99;
    public static String DEVICE_TOKEN = "device_token";


}