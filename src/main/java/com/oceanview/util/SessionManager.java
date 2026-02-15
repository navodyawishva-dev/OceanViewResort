package com.oceanview.util;

import com.oceanview.model.Admin;
import com.oceanview.model.Staff;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class SessionManager {


    private static final String KEY_USER_ID   = "userId";
    private static final String KEY_USERNAME  = "username";
    private static final String KEY_USER_ROLE = "userRole";
    private static final String KEY_USER_OBJ  = "userObj";


    private static final int SESSION_TIMEOUT = 1800;


    public static void createAdminSession(HttpServletRequest request, Admin admin) {
        HttpSession session = request.getSession(true); // create new session
        session.setMaxInactiveInterval(SESSION_TIMEOUT);
        session.setAttribute(KEY_USER_ID,   admin.getId());
        session.setAttribute(KEY_USERNAME,  admin.getUsername());
        session.setAttribute(KEY_USER_ROLE, "Admin");
        session.setAttribute(KEY_USER_OBJ,  admin);
    }


    public static void createStaffSession(HttpServletRequest request, Staff staff) {
        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(SESSION_TIMEOUT);
        session.setAttribute(KEY_USER_ID,   staff.getId());
        session.setAttribute(KEY_USERNAME,  staff.getUsername());
        session.setAttribute(KEY_USER_ROLE, "Staff");
        session.setAttribute(KEY_USER_OBJ,  staff);
    }


    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // don't create new session
        return session != null && session.getAttribute(KEY_USER_ID) != null;
    }


    public static boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        return "Admin".equals(session.getAttribute(KEY_USER_ROLE));
    }


    public static boolean isStaff(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        return "Staff".equals(session.getAttribute(KEY_USER_ROLE));
    }


    public static String getLoggedInUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (String) session.getAttribute(KEY_USER_ID);
    }


    public static String getLoggedInUsername(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (String) session.getAttribute(KEY_USERNAME);
    }


    public static String getLoggedInRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (String) session.getAttribute(KEY_USER_ROLE);
    }


    public static Admin getLoggedInAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        Object obj = session.getAttribute(KEY_USER_OBJ);
        return (obj instanceof Admin) ? (Admin) obj : null;
    }


    public static Staff getLoggedInStaff(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        Object obj = session.getAttribute(KEY_USER_OBJ);
        return (obj instanceof Staff) ? (Staff) obj : null;
    }


    public static void destroySession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // wipes everything
        }
    }
}
