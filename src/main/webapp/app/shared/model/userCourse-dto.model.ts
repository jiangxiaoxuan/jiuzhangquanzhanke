import { CourseDto } from 'app/shared/model/course-dto.model';
import { UserDto } from 'app/shared/model/user-dto.model';

export interface UserCourseDto {
    user: UserDto;
    course: CourseDto;
}
