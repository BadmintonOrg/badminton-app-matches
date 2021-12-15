package si.fri.rso.badmintonappmatches.api.v1;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(info = @Info(title = "Badminton matches played API", version = "v1",
        contact = @Contact(email = "skupina6.rso@student.uni-lj.si"),
        license = @License(name = "dev"), description = "API for managing matches' data for badminton app."))
@ApplicationPath("/v1")
public class BadmintonApplication extends Application {

}
