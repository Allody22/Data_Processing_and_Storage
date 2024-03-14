package ru.nsu.mbogdanov.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
public class SituationsCount {
    private int peopleError = 0;

    //Person
    private int personName = 0;
    private int personId = 0;
    private int personError = 0;

    //ID
    private int idValue = 0;
    private int idVal = 0;
    private int idError = 0;

    //FullName
    private int fullNameFirst = 0;
    private int fullNameFirstnameLow = 0;
    private int fullNameFirstName = 0;
    private int fullNameFamily = 0;
    private int fullNameSecondName = 0;
    private int fullNameSecondnameLow = 0;
    private int fullNameSurname = 0;
    private int fullNameError = 0;

    //First name
    private int firstValue = 0;
    private int firstVal = 0;
    private int first = 0;
    private int firstEmpty = 0;

    private int firstnameLow = 0;
    private int firstnameLowValue = 0;
    private int firstnameLowVal = 0;
    private int firstnameLowEmpty = 0;

    private int firstName = 0;
    private int firstNameValue = 0;
    private int firstNameVal = 0;
    private int firstNameEmpty = 0;

    private int first_name = 0;
    private int first_nameValue = 0;
    private int first_nameVal = 0;
    private int first_nameEmpty = 0;

    //Last name
    private int surname = 0;
    private int surnameValue = 0;
    private int surnameVal = 0;
    private int surnameEmpty = 0;

    private int lastName = 0;
    private int lastNameValue = 0;
    private int lastNameVal = 0;
    private int lastNameError = 0;
    private int lastNameEmpty = 0;

    private int family_name = 0;
    private int family_nameValue = 0;
    private int family_nameVal = 0;
    private int family_nameError = 0;
    private int family_nameEmpty = 0;

    private int patronymic = 0;
    private int patronymicValue = 0;
    private int patronymicVal = 0;
    private int patronymicError = 0;
    private int patronymicEmpty = 0;

    private int family = 0;
    private int familyValue = 0;
    private int familyVal = 0;
    private int familyError = 0;
    private int familyEmpty = 0;

    private int lastnameLow = 0;
    private int lastnameLowValue = 0;
    private int lastnameLowVal = 0;
    private int lastnameLowError = 0;
    private int lastnameLowEmpty = 0;

    private int last_name = 0;
    private int last_nameValue = 0;
    private int last_nameVal = 0;
    private int last_nameError = 0;
    private int last_nameEmpty = 0;

    //Gender
    private int gender = 0;
    private int genderValue = 0;
    private int genderVal = 0;
    private int genderError = 0;
    private int genderEmpty = 0;

    //Spouse
    private int spouseValue = 0;
    private int spouseVal = 0;
    private int spouse = 0;
    private int spouseError = 0;

    private int spouce = 0;
    private int spouceValue = 0;
    private int spouceVal = 0;
    private int spouceError = 0;

    //Husband
    private int husband = 0;
    private int husbandValue = 0;
    private int husbandVal = 0;
    private int husbandError = 0;
    private int husbandEmpty = 0;

    //wife
    private int wife = 0;
    private int wifeValue = 0;
    private int wifeVal = 0;
    private int wifeError = 0;
    private int wifeEmpty = 0;

    //brother
    private int brother = 0;
    private int brotherValue = 0;
    private int brotherVal = 0;
    private int brotherId = 0;
    private int brotherError = 0;
    private int brotherEmpty = 0;

    //sister
    private int sisterValue = 0;
    private int sisterVal = 0;
    private int sisterId = 0;
    private int sister = 0;
    private int sisterError = 0;
    private int sisterEmpty = 0;

    //number
    private int siblings_number = 0;
    private int siblings_number_val = 0;
    private int siblings_number_value = 0;
    private int siblings_number_error = 0;

    //child
    private int childValue = 0;
    private int child = 0;
    private int childVal = 0;
    private int childId = 0;
    private int childError = 0;
    private int childEmpty = 0;

    //son
    private int sonValue = 0;
    private int sonVal = 0;
    private int son = 0;
    private int sonId = 0;
    private int sonError = 0;
    private int sonEmpty = 0;

    //daughter
    private int daughterValue = 0;
    private int daughterVal = 0;
    private int daughter = 0;
    private int daughterId = 0;
    private int daughterError = 0;
    private int daughterEmpty = 0;

    //parent
    private int parentValue = 0;
    private int parentVal = 0;
    private int parentError = 0;


    //father
    private int fatherValue = 0;
    private int fatherVal = 0;
    private int father = 0;
    private int fatherError = 0;
    private int fatherEmpty = 0;

    //mother
    private int motherValue = 0;
    private int motherVal = 0;
    private int mother = 0;
    private int motherEmpty = 0;
    private int motherError = 0;


    //children number
    private int children_number = 0;
    private int children_numberVal = 0;
    private int children_numberValue = 0;
    private int children_numberError = 0;
    private int children_numberEmpty = 0;

    private static class FieldInfo {
        String name;
        int value;

        FieldInfo(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    private FieldInfo createFieldInfo(String fieldName, int fieldValue) {
        return new FieldInfo(fieldName, fieldValue);
    }

    public String beautifulOutputSorted() {
        List<FieldInfo> fields = new ArrayList<>();

        // Добавляем все поля в список
        fields.add(createFieldInfo("People Error", peopleError));

        // Person
        fields.add(createFieldInfo("Person Name", personName));
        fields.add(createFieldInfo("Person ID", personId));
        fields.add(createFieldInfo("Person Error", personError));

        // ID
        fields.add(createFieldInfo("ID Value", idValue));
        fields.add(createFieldInfo("ID Val", idVal));
        fields.add(createFieldInfo("ID Error", idError));

        // FullName
        fields.add(createFieldInfo("Full name Firstname", fullNameFirstnameLow));
        fields.add(createFieldInfo("Full name Secondname", fullNameSecondnameLow));
        fields.add(createFieldInfo("Full Name First", fullNameFirst));
        fields.add(createFieldInfo("Full Name FirstName", fullNameFirstName));
        fields.add(createFieldInfo("Full Name Family", fullNameFamily));
        fields.add(createFieldInfo("Full Name Second Name", fullNameSecondName));
        fields.add(createFieldInfo("Full Name Surname", fullNameSurname));
        fields.add(createFieldInfo("Full Name Error", fullNameError));

        // First name
        fields.add(createFieldInfo("First Value", firstValue));
        fields.add(createFieldInfo("First Val", firstVal));
        fields.add(createFieldInfo("First", first));
        fields.add(createFieldInfo("First Empty", firstEmpty));
        fields.add(createFieldInfo("First name", firstnameLow));
        fields.add(createFieldInfo("First name Value", firstnameLowValue));
        fields.add(createFieldInfo("First name Val", firstnameLowVal));
        fields.add(createFieldInfo("First name Empty", firstnameLowEmpty));
        fields.add(createFieldInfo("First Name", firstName));
        fields.add(createFieldInfo("First Name Value", firstNameValue));
        fields.add(createFieldInfo("First Name Val", firstNameVal));
        fields.add(createFieldInfo("First Name Empty", firstNameEmpty));
        fields.add(createFieldInfo("First_name", first_name));
        fields.add(createFieldInfo("First_name Value", first_nameValue));
        fields.add(createFieldInfo("First_name Val", first_nameVal));
        fields.add(createFieldInfo("First_name Empty", first_nameEmpty));

        // Last name
        fields.add(createFieldInfo("Surname", surname));
        fields.add(createFieldInfo("Surname Value", surnameValue));
        fields.add(createFieldInfo("Surname Val", surnameVal));
        fields.add(createFieldInfo("Surname Empty", surnameEmpty));
        fields.add(createFieldInfo("Last Name", lastName));
        fields.add(createFieldInfo("Last Name Value", lastNameValue));
        fields.add(createFieldInfo("Last Name Val", lastNameVal));
        fields.add(createFieldInfo("Last Name Error", lastNameError));
        fields.add(createFieldInfo("Last Name Empty", lastNameEmpty));
        fields.add(createFieldInfo("Family_name", family_name));
        fields.add(createFieldInfo("Family_name Value", family_nameValue));
        fields.add(createFieldInfo("Family_name Val", family_nameVal));
        fields.add(createFieldInfo("Family_name Error", family_nameError));
        fields.add(createFieldInfo("Family_name Empty", family_nameEmpty));
        fields.add(createFieldInfo("Patronymic", patronymic));
        fields.add(createFieldInfo("Patronymic Value", patronymicValue));
        fields.add(createFieldInfo("Patronymic Val", patronymicVal));
        fields.add(createFieldInfo("Patronymic Error", patronymicError));
        fields.add(createFieldInfo("Patronymic Empty", patronymicEmpty));
        fields.add(createFieldInfo("Family", family));
        fields.add(createFieldInfo("Family Value", familyValue));
        fields.add(createFieldInfo("Family Val", familyVal));
        fields.add(createFieldInfo("Family Error", familyError));
        fields.add(createFieldInfo("Family Empty", familyEmpty));
        fields.add(createFieldInfo("Lastname", lastnameLow));
        fields.add(createFieldInfo("Lastname Value", lastnameLowValue));
        fields.add(createFieldInfo("Lastname Val", lastnameLowVal));
        fields.add(createFieldInfo("Lastname Error", lastnameLowError));
        fields.add(createFieldInfo("Lastname Empty", lastnameLowEmpty));
        fields.add(createFieldInfo("Last_name", last_name));
        fields.add(createFieldInfo("Last_name Value", last_nameValue));
        fields.add(createFieldInfo("Last_name Val", last_nameVal));
        fields.add(createFieldInfo("Last_name Error", last_nameError));
        fields.add(createFieldInfo("Last_name Empty", last_nameEmpty));

        // Gender
        fields.add(createFieldInfo("Gender", gender));
        fields.add(createFieldInfo("Gender Value", genderValue));
        fields.add(createFieldInfo("Gender Val", genderVal));
        fields.add(createFieldInfo("Gender Error", genderError));
        fields.add(createFieldInfo("Gender Empty", genderEmpty));

        // Spouse
        fields.add(createFieldInfo("Spouse Value", spouseValue));
        fields.add(createFieldInfo("Spouse Val", spouseVal));
        fields.add(createFieldInfo("Spouse", spouse));
        fields.add(createFieldInfo("Spouse Error", spouseError));
        fields.add(createFieldInfo("Spouce", spouce));
        fields.add(createFieldInfo("Spouce Value", spouceValue));
        fields.add(createFieldInfo("Spouce Val", spouceVal));
        fields.add(createFieldInfo("Spouce Error", spouceError));

        // Husband
        fields.add(createFieldInfo("Husband", husband));
        fields.add(createFieldInfo("Husband Value", husbandValue));
        fields.add(createFieldInfo("Husband Val", husbandVal));
        fields.add(createFieldInfo("Husband Error", husbandError));
        fields.add(createFieldInfo("Husband Empty", husbandEmpty));

        // Wife
        fields.add(createFieldInfo("Wife", wife));
        fields.add(createFieldInfo("Wife Value", wifeValue));
        fields.add(createFieldInfo("Wife Val", wifeVal));
        fields.add(createFieldInfo("Wife Error", wifeError));
        fields.add(createFieldInfo("Wife Empty", wifeEmpty));

        // Brother
        fields.add(createFieldInfo("Brother", brother));
        fields.add(createFieldInfo("Brother Value", brotherValue));
        fields.add(createFieldInfo("Brother Val", brotherVal));
        fields.add(createFieldInfo("Brother ID", brotherId));
        fields.add(createFieldInfo("Brother Error", brotherError));
        fields.add(createFieldInfo("Brother Empty", brotherEmpty));

        // Sister
        fields.add(createFieldInfo("Sister Value", sisterValue));
        fields.add(createFieldInfo("Sister Val", sisterVal));
        fields.add(createFieldInfo("Sister ID", sisterId));
        fields.add(createFieldInfo("Sister", sister));
        fields.add(createFieldInfo("Sister Error", sisterError));
        fields.add(createFieldInfo("Sister Empty", sisterEmpty));

        //Siblings Number
        fields.add(createFieldInfo("Siblings Number", siblings_number));
        fields.add(createFieldInfo("Siblings Number Val", siblings_number_val));
        fields.add(createFieldInfo("Siblings Number Value", siblings_number_value));
        fields.add(createFieldInfo("Siblings Number Error", siblings_number_error));

        // Child
        fields.add(createFieldInfo("Child Value", childValue));
        fields.add(createFieldInfo("Child", child));
        fields.add(createFieldInfo("Child Val", childVal));
        fields.add(createFieldInfo("Child ID", childId));
        fields.add(createFieldInfo("Child Error", childError));
        fields.add(createFieldInfo("Child Empty", childEmpty));

        // Son
        fields.add(createFieldInfo("Son Value", sonValue));
        fields.add(createFieldInfo("Son Val", sonVal));
        fields.add(createFieldInfo("Son", son));
        fields.add(createFieldInfo("Son ID", sonId));
        fields.add(createFieldInfo("Son Error", sonError));
        fields.add(createFieldInfo("Son Empty", sonEmpty));

        // Daughter
        fields.add(createFieldInfo("Daughter", daughter));
        fields.add(createFieldInfo("Daughter Value", daughterValue));
        fields.add(createFieldInfo("Daughter Val", daughterVal));
        fields.add(createFieldInfo("Daughter ID", daughterId));
        fields.add(createFieldInfo("Daughter Error", daughterError));
        fields.add(createFieldInfo("Daughter Empty", daughterEmpty));

        // Parent
        fields.add(createFieldInfo("Parent Value", parentValue));
        fields.add(createFieldInfo("Parent Val", parentVal));
        fields.add(createFieldInfo("Parent Error", parentError));

        // Father
        fields.add(createFieldInfo("Father Value", fatherValue));
        fields.add(createFieldInfo("Father Val", fatherVal));
        fields.add(createFieldInfo("Father", father));
        fields.add(createFieldInfo("Father Error", fatherError));
        fields.add(createFieldInfo("Father Empty", fatherEmpty));

        // Mother
        fields.add(createFieldInfo("Mother", mother));
        fields.add(createFieldInfo("Mother Value", motherValue));
        fields.add(createFieldInfo("Mother Val", motherVal));
        fields.add(createFieldInfo("Mother Error", motherError));
        fields.add(createFieldInfo("Mother Empty", motherEmpty));

        // Children number
        fields.add(createFieldInfo("Children Number", children_number));
        fields.add(createFieldInfo("Children Number Val", children_numberVal));
        fields.add(createFieldInfo("Children Number Value", children_numberValue));
        fields.add(createFieldInfo("Children Number Error", children_numberError));
        fields.add(createFieldInfo("Children Number Empty", children_numberEmpty));

        fields.sort(Comparator.comparingInt(f -> (f.value == 0) ? 0 : 1));

        StringBuilder sb = new StringBuilder();
        sb.append("Situations Count Details:\n");

        for (FieldInfo field : fields) {
            sb.append(field.name).append(": ");
            if (field.value == 0) {
                sb.append("Нулевое значение\n");
            } else {
                sb.append(field.value).append("\n");
            }
        }

        return sb.toString();
    }

}
