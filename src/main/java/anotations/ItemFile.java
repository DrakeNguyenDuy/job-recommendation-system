package anotations;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import org.lenskit.inject.Parameter;

/*
 * "@Documented": Xác định rằng Annotation "RatingFile" sẽ được ghi lại trong tài liệu API của thư viện Lenskit.

 * "@Retention": Xác định thời điểm mà Annotation "RatingFile" sẽ được giữ lại. Trong trường hợp này, Annotation "RatingFile" sẽ được giữ lại trong thời gian chạy của chương trình.

 * "@Target": Xác định các thành phần trong chương trình mà Annotation "RatingFile" có thể được sử dụng. Trong trường hợp này, Annotation "RatingFile" chỉ có thể được sử dụng với các tham số của phương thức.

 * "@Qualifier": Đây là một Annotation được sử dụng để phân biệt các Annotation khác với nhau. Trong trường hợp này, Annotation "RatingFile" được sử dụng để đánh dấu một tham số của phương thức.

 * "@Parameter": Đây là một Annotation của thư viện Lenskit, được sử dụng để chỉ định kiểu dữ liệu của tham số. Trong trường hợp này, kiểu dữ liệu của tham số được định nghĩa là "File.class", tức là tham số phải là một đối tượng File.
 */
@Qualifier
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(File.class)
public @interface ItemFile {

}
