package ca.uwindsor.ims;

public final class Constants {

    private Constants() {}

    // ── JWT claim keys ──────────────────────────────────────────────────────────
    public static final String JWT_CLAIM_ROLE       = "role";
    public static final String JWT_CLAIM_STUDENT_ID = "student_id";

    // ── User types (stored in vbct_login.user_type) ─────────────────────────────
    public static final String USER_TYPE_ADMIN   = "admin";
    public static final String USER_TYPE_STUDENT = "Student";

    // ── Login flag codes ────────────────────────────────────────────────────────
    public static final String FLAG_ACTIVE  = "A";
    public static final String FLAG_PENDING = "N";

    // ── Internship / student statuses ───────────────────────────────────────────
    public static final String INTERNSHIP_STATUS_PENDING = "Pending";
    public static final String STUDENT_STATUS_ACTIVE     = "Active";
    public static final String STUDENT_STATUS_HIRED      = "Hired";
}
