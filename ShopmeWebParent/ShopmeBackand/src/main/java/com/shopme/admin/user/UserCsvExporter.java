package com.shopme.admin.user;

import com.shopme.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {

    public void export(List<User> users, HttpServletResponse response) throws IOException {
       super.setResponseHeader(response,".csv", "text/csv");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"User ID", "E-mail", "First Name","Last Name", "Roles", "Enabled"};
        String[] fieldMapping = {"id", "email","firstName", "lastName","roles", "enabled"};
        csvWriter.writeHeader(csvHeader);

        for (User user : users){
            csvWriter.write(user,fieldMapping);
        }
        csvWriter.close();

    }
}
