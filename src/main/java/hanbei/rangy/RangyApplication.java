package hanbei.rangy;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class RangyApplication extends Application<RangyConfiguration> {
    @Override
    public void run(RangyConfiguration configuration, Environment environment) {
        environment.jersey().register(new RangeResource());
    }

    public static void main(String[] args) throws Exception {
        new RangyApplication().run(args);
    }
}
