package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Course;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.UserCourse;
import com.mycompany.myapp.domain.dto.CourseDto;
import com.mycompany.myapp.domain.dto.CourseWithTNDto;
import com.mycompany.myapp.domain.dto.TeacherDto;
import com.mycompany.myapp.repository.CourseRepository;
import com.mycompany.myapp.repository.UserCourseRepository;
import com.zaxxer.hikari.HikariDataSource;

import org.checkerframework.checker.units.qual.A;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {
    private final Logger log = LoggerFactory.getLogger(CourseService.class);
    
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private UserService userService;

    public List<CourseDto> findAllCourses(){
        List<Course> courses= courseRepository.findAll();

        List<CourseDto> courseDtos = new ArrayList<>();

        for(Course c : courses) {
            courseDtos.add(new CourseDto(c.getCourseName(), c.getCourseLocation(), c.getCourseContent(), c.getTeacherId()));
        }

        return courseDtos;
    }

    public List<CourseDto> findAllCoursesDtoFromDB(){
    	List<TeacherDto> teachers = courseRepository.findTeachers();
    	for (TeacherDto teacher : teachers) {
    		log.debug("Teacher first name!!!!!!" + teacher.getFirstName());
    	}
    	
    	List<User> teachers3 = userService.allTeachers();
    	for (User u : teachers3) {
    		log.debug("Teacher2 first name!!!!!!" + u.getFirstName());
    	}
    	

//        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
//        sessionBuilder.scanPackages("com.mycompany.myapp.domain");
//        SessionFactory sessionFactory = sessionBuilder.buildSessionFactory();
//    	Session session = sessionFactory.openSession();
//    	List<User> teachers2 = session.createCriteria(User.class).add(Restrictions.eq("user_type", "teacher")).list();
//    	for (User u : teachers2) {
//    		log.debug("Teacher2 first name!!!!!!" + u.getFirstName());
//    	}
//    	
        return courseRepository.findAllCoursesDto();
    }

    public List<CourseWithTNDto> findAllCoursesDtoWithTeacherNameFromDB(){
        return courseRepository.findAllCoursesDtoWithTeacherName();
    }


    public void registerCourse(String courseName) throws Exception{
        Optional<User> curUser = userService.getUserWithAuthorities();
        Optional<Course> curCourse = courseRepository.findCourseByCourseName(courseName);

        if (curUser.isPresent() && curCourse.isPresent()){
            userCourseRepository.save(UserCourse.builder()
                .user(curUser.get())
                .course(curCourse.get())
                .build());
        } else {
            throw new Exception("UnExpected Exception");
        }
    }
}
