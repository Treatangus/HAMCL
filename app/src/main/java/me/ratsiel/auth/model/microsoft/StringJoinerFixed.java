package me.ratsiel.auth.model.microsoft;


import java.util.Objects;

public final class StringJoinerFixed {
    private  String prefix;//前缀
    private  String delimiter;//间隔符
    private  String suffix;//后缀
    private StringBuilder value;//值

    private String emptyValue;//空值

    public StringJoinerFixed(CharSequence delimiter) {
        this(delimiter, "", "");//默认前缀和后缀为"",重载调用
    }

    public StringJoinerFixed(CharSequence delimiter,
                        CharSequence prefix,
                        CharSequence suffix) {
        //间隔符，前缀和后缀判断是否为null，null将抛出异常
        Objects.requireNonNull(prefix, "The prefix must not be null");
        Objects.requireNonNull(delimiter, "The delimiter must not be null");
        Objects.requireNonNull(suffix, "The suffix must not be null");
        // 成员变量赋值
        this.prefix = prefix.toString();
        this.delimiter = delimiter.toString();
        this.suffix = suffix.toString();
        this.emptyValue = this.prefix + this.suffix;//空值被设置为只有前后缀
    }

    public StringJoinerFixed () {
    }

    //设置空值，检查是否为null
    public StringJoinerFixed setEmptyValue(CharSequence emptyValue) {
        this.emptyValue = Objects.requireNonNull(emptyValue,
                "The empty value must not be null").toString();
        return this;
    }

    @Override
    public String toString() {
        if (value == null) {
            return emptyValue;//没有值将返回空值或者后续设置的空值
        } else {
            if (suffix.equals("")) {
                return value.toString();//后缀为""直接返回字符串，不用添加
            } else {
                //后缀不为""，添加后缀，然后直接返回字符串，修改长度
                int initialLength = value.length();
                String result = value.append(suffix).toString();
                // reset value to pre-append initialLength
                value.setLength(initialLength);
                return result;
            }
        }
    }
   // 初始化，先添加前缀，有了之后每次先添加间隔符，StringBuilder后续append字符串
    public StringJoinerFixed add(CharSequence newElement) {
        prepareBuilder().append(newElement);
        return this;
    }
    //合并StringJoiner，注意后面StringJoiner 的前缀就不要了，后面的appen进来
    public StringJoinerFixed merge(StringJoinerFixed other) {
        Objects.requireNonNull(other);
        if (other.value != null) {
            final int length = other.value.length();
            // lock the length so that we can seize the data to be appended
            // before initiate copying to avoid interference, especially when
            // merge 'this'
            StringBuilder builder = prepareBuilder();
            builder.append(other.value, other.prefix.length(), length);
        }
        return this;
    }
    //初始化，先添加前缀，有了之后每次先添加间隔符
    private StringBuilder prepareBuilder() {
        if (value != null) {
            value.append(delimiter);
        } else {
            value = new StringBuilder().append(prefix);
        }
        return value;
    }

    public int length() {
        // Remember that we never actually append the suffix unless we return
        // the full (present) value or some sub-string or length of it, so that
        // we can add on more if we need to.
        //不忘添加后缀的长度
        return (value != null ? value.length() + suffix.length() :
                emptyValue.length());
    }
}
