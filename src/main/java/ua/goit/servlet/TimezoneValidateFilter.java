package ua.goit.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.TimeZone;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/time")
public class TimezoneValidateFilter extends HttpFilter {
    public TimezoneValidateFilter() {
    }

    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String timezoneParam = request.getParameter("timezone");
        if (timezoneParam != null && !this.isValidTimezone(timezoneParam)) {
            response.setStatus(400);
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            try {
                out.write("<html>");
                out.write("<head><title>Invalid Timezone</title></head>");
                out.write("<body>");
                out.write("<h1>Invalid timezone</h1>");
                out.write("</body>");
                out.write("</html>");
            } catch (Throwable var9) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                }

                throw var9;
            }

            if (out != null) {
                out.close();
            }

        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isValidTimezone(String timezone) {
        if (timezone == null) {
            return false;
        } else {
            int getUTC;
            if (timezone.startsWith("UTC ")) {
                try {
                    getUTC = Integer.parseInt(timezone.substring(4));
                    return getUTC >= -12 && getUTC <= 12;
                } catch (NumberFormatException var3) {
                    return false;
                }
            } else if (!timezone.startsWith("UTC-")) {
                return TimeZone.getTimeZone(timezone).getID().equals(timezone);
            } else {
                try {
                    getUTC = Integer.parseInt(timezone.substring(4));
                    return getUTC >= -12 && getUTC <= 12;
                } catch (NumberFormatException var4) {
                    return false;
                }
            }
        }
    }
}
