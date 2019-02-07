import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LoginModalService, Principal, Account } from 'app/core';
import { CourseService } from 'app/shared/service/CourseService';
import { CourseDto } from 'app/shared/model/course-dto.model';
import { CourseWithTNDto } from 'app/shared/model/courseWithTN-dto.model';
import { UserCourseDto } from 'app/shared/model/userCourse-dto.model';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.css']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    classeNameNeedToReg: string;
    newCourseName: string;
    newCourseLocation: string;
    newCourseContent: string;
    newCourseTeacher: number;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private courseService: CourseService
    ) {}

    courses: CourseDto[] = [];

    coursesWithTN: CourseWithTNDto[] = [];

    userCourses: UserCourseDto[] = [];

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    isAdmin() {
        for (const a of this.account.authorities) {
            if (a === 'ROLE_ADMIN') return true;
        }
        return false;
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    getAllCourses() {
        this.courseService.getCourseInfo().subscribe(curDto => {
            if (!curDto) {
                this.courses = [];
            } else {
                this.courses = curDto;
            }
        });
    }

    getUserCourses() {
        this.courseService.userCourse().subscribe(userCourseDto => {
            if (!userCourseDto) {
                this.userCourses = [];
            } else {
                this.userCourses = userCourseDto;
            }
        });
    }

    deleteCourse(courseName: String) {
        this.courseService.delete(courseName).subscribe(response => {
            if (response.ok == false) {
                return;
            }

            this.courses = this.courses.filter(course => course.courseName !== courseName);
        });
    }

    registerCourse(courseName: String) {
        this.courseService.registerCourse(courseName).subscribe(response => {
            if (response.ok == false) {
                return;
            }
            this.getUserCourses();
        });
    }

    addCourse() {
        var newCourse: CourseDto = {
            courseName: this.newCourseName,
            courseLocation: this.newCourseLocation,
            courseContent: this.newCourseContent,
            teacherId: this.newCourseTeacher
        };
        this.courseService.add(newCourse).subscribe(response => {
            if (response.ok == false) {
                return;
            } else {
                this.courses.push(newCourse);
            }
        });
    }

    dropCourse(courseName: String) {
        this.courseService.drop(courseName).subscribe(response => {
            this.getUserCourses();
        });
    }

    clearAllCourses() {
        this.courses = [];
    }

    clearUserCourses() {
        this.userCourses = [];
    }
}
