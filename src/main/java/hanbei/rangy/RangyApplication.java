package hanbei.rangy;

import io.dropwizard.Application;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class RangyApplication extends Application<RangyConfiguration> {

    @Override
    public void initialize(Bootstrap<RangyConfiguration> bootstrap) {
        bootstrap.addBundle(new MultiPartBundle());
    }

    @Override
    public void run(RangyConfiguration configuration, Environment environment) {
        environment.jersey().register(new RangeResource());
    }

    public static void main(String[] args) throws Exception {
        new RangyApplication().run(args);
    }
}
