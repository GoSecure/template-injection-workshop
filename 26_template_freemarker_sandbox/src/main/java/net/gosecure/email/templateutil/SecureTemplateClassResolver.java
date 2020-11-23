package net.gosecure.email.templateutil;

import freemarker.core.Environment;
import freemarker.core.TemplateClassResolver;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.utility.Execute;
import freemarker.template.utility.ObjectConstructor;
import net.gosecure.email.util.AggregateClassLoader;
import net.gosecure.email.util.ArrayUtil;

public class SecureTemplateClassResolver implements TemplateClassResolver {

    public static final String STAR = "*";

    private final Configuration cfg;

    private static String[] restrictedClassNames = {};
    private static String[] allowedClasseNames = {};

    public SecureTemplateClassResolver(Configuration cfg) {
        this.cfg = cfg;
    }

    @Override
    public Class resolve(String className, Environment environment, Template template) throws TemplateException {
        if (className.equals(Execute.class.getName()) ||
                className.equals(ObjectConstructor.class.getName())) {

            throw new TemplateException(
                    stringConcat(
                            "Instantiating ", className, " is not allowed in the ",
                            "template for security reasons"),
                    environment);
        }
/*
        for (String restrictedClassName : restrictedClassNames) {
            if (match(restrictedClassName, className)) {
                throw new TemplateException(
                        stringConcat("Instantiating ", className, " is not allowed in the template for security reasons"),
                        environment);
            }
        }

        boolean allowed = false;

        for (String allowedClassName : allowedClasseNames) {
            if (match(allowedClassName, className)) {
                allowed = true;

                break;
            }
        }
        */
        boolean allowed = true;
        if (allowed) {
            try {
                ClassLoader[] wwhitelistedClassLoaders = new ClassLoader[0];

                Thread currentThread = Thread.currentThread();

                ClassLoader[] classLoaders = ArrayUtil.append(
                        wwhitelistedClassLoaders,
                        currentThread.getContextClassLoader());

                ClassLoader whitelistedAggregateClassLoader =
                        AggregateClassLoader.getAggregateClassLoader(classLoaders);

                return Class.forName(
                        className, true, whitelistedAggregateClassLoader);
            }
            catch (Exception exception) {
                throw new TemplateException(exception, environment);
            }
        }

        throw new TemplateException(stringConcat("Instantiating ", className, " is not allowed in the template ",
            "for security reasons"), environment);
    }

    private String stringConcat(String... args) {
        StringBuilder buffer = new StringBuilder();
        for(String str : args) {
            buffer.append(str);
        }
        return buffer.toString();
    }


    protected boolean match(String className, String matchedClassName) {
        if (className.equals(STAR)) {
            return true;
        }
        else if (className.endsWith(STAR)) {
            if (matchedClassName.regionMatches(
                    0, className, 0, className.length() - 1)) {

                return true;
            }
        }
        else if (className.equals(matchedClassName)) {
            return true;
        }
        else {
            int index = className.lastIndexOf('.');

            if ((className.length() == index) &&
                    className.regionMatches(0, matchedClassName, 0, index)) {
                return true;
            }
        }

        return false;
    }

}
