package net.gosecure.email.util;


import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * @author Shuyang Zhou
 */
public class EqualityWeakReference<T> extends WeakReference<T> {

    public EqualityWeakReference(T referent) {
        super(referent);

        _hashCode = referent.hashCode();
    }

    public EqualityWeakReference(
            T referent, ReferenceQueue<? super T> referenceQueue) {

        super(referent, referenceQueue);

        _hashCode = referent.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof EqualityWeakReference<?>)) {
            return false;
        }

        EqualityWeakReference<?> equalityWeakReference =
                (EqualityWeakReference<?>)object;

        if (Objects.equals(get(), equalityWeakReference.get())) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return _hashCode;
    }

    private final int _hashCode;

}
