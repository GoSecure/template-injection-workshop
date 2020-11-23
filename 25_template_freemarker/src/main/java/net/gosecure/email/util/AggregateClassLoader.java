package net.gosecure.email.util;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author Brian Wing Shun Chan
 * @author Michael C. Han
 * @author Shuyang Zhou
 */
public class AggregateClassLoader extends ClassLoader {

    public static ClassLoader getAggregateClassLoader(
            ClassLoader parentClassLoader, ClassLoader... classLoaders) {

        if (classLoaders == null || classLoaders.length == 0) {
            return parentClassLoader;
        }

        AggregateClassLoader aggregateClassLoader = null;

        if (parentClassLoader instanceof AggregateClassLoader) {
            aggregateClassLoader = (AggregateClassLoader)parentClassLoader;

            List<ClassLoader> existingClassLoaders =
                    aggregateClassLoader.getClassLoaders();

            boolean requiresNew = false;

            for (ClassLoader classLoader : classLoaders) {
                if (!classLoader.equals(parentClassLoader) &&
                        !existingClassLoaders.contains(classLoader)) {

                    requiresNew = true;

                    break;
                }
            }

            if (!requiresNew) {
                return aggregateClassLoader;
            }

            aggregateClassLoader = new AggregateClassLoader(
                    parentClassLoader.getParent());

            aggregateClassLoader.addClassLoader(parentClassLoader);
        }
        else {
            aggregateClassLoader = new AggregateClassLoader(parentClassLoader);
        }

        for (ClassLoader classLoader : classLoaders) {
            aggregateClassLoader.addClassLoader(classLoader);
        }

        return aggregateClassLoader;
    }

    public static ClassLoader getAggregateClassLoader(
            ClassLoader[] classLoaders) {

        if (classLoaders == null || classLoaders.length == 0) {
            return null;
        }

        return getAggregateClassLoader(classLoaders[0], classLoaders);
    }

    public AggregateClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }

    public void addClassLoader(ClassLoader classLoader) {
        if (classLoader.equals(getParent())) {
            return;
        }

        List<ClassLoader> classLoaders = getClassLoaders();

        if (classLoaders.contains(classLoader)) {
            return;
        }

        if (classLoader instanceof AggregateClassLoader) {
            AggregateClassLoader aggregateClassLoader =
                    (AggregateClassLoader)classLoader;

            addClassLoader(aggregateClassLoader.getParent());

            for (ClassLoader curClassLoader :
                    aggregateClassLoader.getClassLoaders()) {

                addClassLoader(curClassLoader);
            }

            return;
        }

        _classLoaderReferences.add(new EqualityWeakReference<>(classLoader));
    }

    public void addClassLoader(ClassLoader... classLoaders) {
        for (ClassLoader classLoader : classLoaders) {
            addClassLoader(classLoader);
        }
    }

    public void addClassLoader(Collection<ClassLoader> classLoaders) {
        for (ClassLoader classLoader : classLoaders) {
            addClassLoader(classLoader);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof AggregateClassLoader)) {
            return false;
        }

        AggregateClassLoader aggregateClassLoader =
                (AggregateClassLoader)object;

        if (_classLoaderReferences.equals(
                aggregateClassLoader._classLoaderReferences) &&
                Objects.equals(getParent(), aggregateClassLoader.getParent())) {

            return true;
        }

        return false;
    }

    public List<ClassLoader> getClassLoaders() {
        List<ClassLoader> classLoaders = new ArrayList<>(
                _classLoaderReferences.size());

        Iterator<EqualityWeakReference<ClassLoader>> iterator = _classLoaderReferences.iterator();

        while (iterator.hasNext()) {
            WeakReference<ClassLoader> weakReference = iterator.next();

            ClassLoader classLoader = weakReference.get();

            if (classLoader == null) {
                iterator.remove();
            }
            else {
                classLoaders.add(classLoader);
            }
        }

        return classLoaders;
    }

    @Override
    public URL getResource(String name) {
        for (ClassLoader classLoader : getClassLoaders()) {
            URL url = classLoader.getResource(name);

            if (url != null) {
                return url;
            }
        }

        return super.getResource(name);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        List<URL> urls = new ArrayList<>();

        for (ClassLoader classLoader : getClassLoaders()) {
            urls.addAll(Collections.list(classLoader.getResources(name)));
        }

        ClassLoader parentClassLoader = getParent();

        urls.addAll(Collections.list(parentClassLoader.getResources(name)));

        return Collections.enumeration(urls);
    }

    @Override
    public int hashCode() {
        int hash = HashUtil.hash(0, _classLoaderReferences);

        return HashUtil.hash(hash, getParent());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        for (ClassLoader classLoader : getClassLoaders()) {
            try {
                return _findClass(classLoader, name);
            }
            catch (ClassNotFoundException classNotFoundException) {
            }
        }

        throw new ClassNotFoundException("Unable to find class " + name);
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException {

        Class<?> loadedClass = null;

        for (ClassLoader classLoader : getClassLoaders()) {
            try {
                loadedClass = _loadClass(classLoader, name, resolve);

                break;
            }
            catch (ClassNotFoundException classNotFoundException) {
            }
        }

        if (loadedClass == null) {
            loadedClass = _loadClass(getParent(), name, resolve);
        }
        else if (resolve) {
            resolveClass(loadedClass);
        }

        return loadedClass;
    }

    private Class<?> _findClass(ClassLoader classLoader, String name)
            throws ClassNotFoundException {

        try {
            return (Class<?>)_FIND_CLASS_METHOD.invoke(classLoader, name);
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new ClassNotFoundException(
                    "Unable to find class " + name,
                    invocationTargetException.getTargetException());
        }
        catch (Exception exception) {
            throw new ClassNotFoundException(
                    "Unable to find class " + name, exception);
        }
    }

    private Class<?> _loadClass(
            ClassLoader classLoader, String name, boolean resolve)
            throws ClassNotFoundException {

        if (resolve) {
            try {
                return (Class<?>)_LOAD_CLASS_METHOD.invoke(
                        classLoader, name, true);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new ClassNotFoundException(
                        "Unable to load class " + name,
                        invocationTargetException.getTargetException());
            }
            catch (Exception exception) {
                throw new ClassNotFoundException(
                        "Unable to load class " + name, exception);
            }
        }

        return classLoader.loadClass(name);
    }

    private static final Method _FIND_CLASS_METHOD;

    private static final Method _LOAD_CLASS_METHOD;

    static {
        try {
            _FIND_CLASS_METHOD = ReflectionUtil.getDeclaredMethod(
                    ClassLoader.class, "findClass", String.class);

            _LOAD_CLASS_METHOD = ReflectionUtil.getDeclaredMethod(
                    ClassLoader.class, "loadClass", String.class, boolean.class);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private final List<EqualityWeakReference<ClassLoader>>
            _classLoaderReferences = new ArrayList<>();

}
