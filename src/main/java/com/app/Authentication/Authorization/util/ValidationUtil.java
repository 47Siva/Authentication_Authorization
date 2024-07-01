package com.app.Authentication.Authorization.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.app.Authentication.Authorization.enumeration.GenderType;
import com.app.Authentication.Authorization.enumeration.Role;

public class ValidationUtil {

	private ValidationUtil() {

	}

	public static boolean isNullOrEmpty(String value) {
		return null == value || value.trim().isEmpty();
	}

	public static boolean isNull(UUID value) {
		return null == value;
	}

	public static boolean isNull(Double value) {
		return null == value || value == 0;
	}

	public static Boolean isValidPhoneNo(String phoneNo) {
		if (phoneNo.length() < 10 || phoneNo.length() > 10) {
			return true;
		}
		return false;
	}

	public static boolean isValidMobileNumber(String value) {
		String regex = "(?:\\s+|)((0|(?:(\\+|)91))(?:\\s|-)*(?:(?:\\d(?:\\s|-)*\\d{9})|(?:\\d{2}(?:\\s|-)*\\d{8})|(?:\\d{3}(?:\\s|-)*\\d{7}))|\\d{10})(?:\\s+|)";
		Pattern p = Pattern.compile(regex);
		String s = String.valueOf(value);
		Matcher m = p.matcher(s);
		return m.matches();
	}

	public static boolean isValidEmailId(String value) {
		String regex = "^(?=.{1,64}@)[a-zA-Z][a-zA-Z0-9]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9-.]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(value);
		return m.matches();
	}

	public static boolean isNameValid(String name) {
		return name != null && !name.isEmpty();
	}

	public static boolean isValidName(String name) {
		if (name == null || name.isEmpty()) {
			return false;
		}

		// Define the allowed name pattern
		String namePattern = "^[\\p{L} '-]+$";

		// Check if the name matches the pattern
		if (!name.matches(namePattern)) {
			return false;
		}

		// Check for length constraints
		if (name.length() < 1 || name.length() > 50) {
			return false;
		}

		return true;
	}

	public static boolean isUserNameValid(String userName) {
		if (userName == null || userName.length() < 3 || userName.length() > 20) {
			return false;
		}
		return userName.matches("^[a-zA-Z][a-zA-Z0-9]+$");

//		IF THE USERNAME IS "USER123", IT WILL RETURN TRUE BECAUSE IT MEETS ALL THE CRITERIA.
//		IF THE USERNAME IS "USER!123", IT WILL RETURN FALSE BECAUSE IT CONTAINS A SPECIAL CHARACTER (!) WHICH IS NOT ALLOWED.
	}

	public static boolean isValidAddress(String value) {
		String regex = "^(\\w*\\s*[\\:\\#\\-\\,\\/\\.\\(\\)\\&]*)+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(value);
		return m.matches();
	}

	public static boolean isGenderrequired(GenderType gender) {
		return gender == null;
	}
	
	public static boolean isRolerequired(Role role) {
		return role == null;
	}

	public static boolean isGenderValid(GenderType gender) {
		String genderTypetoString = gender.toString();
		return genderTypetoString.equalsIgnoreCase("male") || genderTypetoString.equalsIgnoreCase("female")
				|| genderTypetoString.equalsIgnoreCase("other");
	}
	
	public static boolean isRoleValid(Role role) {
		String roleTypetoString = role.toString();
		return roleTypetoString.equalsIgnoreCase("admin") || roleTypetoString.equalsIgnoreCase("user");
	}

	
	
	public static boolean isValidPassword(String password) {
		if (password == null) {
			return false;
		}

		String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";

		if (!Pattern.matches(passwordRegex, password)) {
			return false;
		}

		boolean hasSpecialChar = password.contains("@");

		return hasSpecialChar;
	}

	public static boolean isPasswordValid(String password, String confirmPassword) {
		// Check if both password and confirmPassword are not null and are equal
		if (password == null || confirmPassword == null || !password.equals(confirmPassword)) {
			return false;
		}

		// Regex that checks for the minimum requirements
		String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";

		// Use Pattern.matches to check if the password meets the regex requirements
		if (!Pattern.matches(passwordRegex, password)) {
			return false;
		}

		// Optional: Check for specific special characters if needed
		// For example, to ensure that the password contains at least one '@' character
		boolean hasSpecialChar = password.contains("@");

		return hasSpecialChar;
	}

	public static boolean isDateOfBirthrequired(String dateOfBirth) {
		return dateOfBirth == null || dateOfBirth.isEmpty();
	}

	public static boolean isValueNegative(Number values) {
		if (values instanceof Double) {
			Double value = (Double) values;
			if (value.doubleValue() < 0) {
				return true;
			}
		}
		if (values instanceof Integer) {
			Integer value = (Integer) values;
			if (value.intValue() < 0) {
				return true;
			}
		}
		return false;
	}

	// Added trim function for NotnullorEmpty Elements
	public static String getFormattedString(String value) {
		if (value != null) {
			return value.trim();
		}
		return value;
	}

	public static boolean isDateOfBirthValid(String dob) {
		final String DATE_PATTERN = "dd-MM-yyyy";

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
		LocalDate date = LocalDate.parse(dob, formatter);

		if (dob == null || dob.isEmpty()) {
			return false;
		}
		if (date.isAfter(LocalDate.now())) {
			return false;
		}
		return true;

	}

	public static boolean isAgerequired(String age) {
		return age == null || age.isEmpty();
	}

	public static int calculateAge(String dob) {
		LocalDate today = LocalDate.now();
		final String DATE_PATTERN = "dd-MM-yyyy";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
		LocalDate date = LocalDate.parse(dob, formatter);
		return Period.between(date, today).getYears();
	}

	public static boolean isDateOfBirthAgeMatcher(String dob, String reportedAge) {
		int calculatedAge = calculateAge(dob);
		int age = Integer.parseInt(reportedAge);
		return calculatedAge == age;
	}

	public static boolean isEighteenOrOlder(String birthDate, String reportedAge) {

		int calculatedAge = calculateAge(birthDate);

		int age = Integer.parseInt(reportedAge);

		return calculatedAge >= 18 && age > 18;
	}

	public static String getformatDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate date1 = LocalDate.parse(date, formatter);
		return date1.format(formatter);
	}

	public static GenderType getFormattedGender(GenderType gender) {
		switch (gender) {
		case MALE:
			return GenderType.MALE;
		case FEMALE:
			return GenderType.FEMALE;
		case OTHER:
			return GenderType.OTHER;
		}
		return null;
	}

	public static Role getFormattedRole(Role role) {
		switch (role) {
		case USER:
			return Role.USER;
		case ADMIN:
			return Role.ADMIN;
		}
		return null;
	}

    public static Role getFormattedRoles(String role) {
        try {
            return Role.fromString(role);
        } catch (IllegalArgumentException e) {
            return null; // Return null if role is invalid
        }
    }
	
	public static boolean isNullObject(Role value) {
		return null == value;
	}

}
