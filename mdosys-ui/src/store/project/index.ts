import { defineStore } from "pinia";
import { ProjectItem } from "@/apis/Project/project";
import { delProjectByCode, queryProjectByCode, queryProjectCreatedAndAuthorizedByUser, updateProject } from "@/apis/Project/projectAction";
export const useProjectsStore = defineStore('projectsStore', {
    state: () => {
        return {
            projects: new Array<ProjectItem>(),
        }
    },
    getters: {
    },
    actions: {
        async getUserProjectList() {
            try {
                this.projects.length = 0;
                const res = await queryProjectCreatedAndAuthorizedByUser();
                let list = res.data;
                this.projects.push(...list);
                return Promise.resolve(list);
            } catch (error) {
                return Promise.reject(error);
            }
        },
        async getProjectDetail(projectCode: string) {
            let index=-1;
            for (let i = 0; i < this.projects.length; i++) {
                if (projectCode == this.projects[i].code) {
                    index=i;
                    break;
                }
            }
            const res = await queryProjectByCode(projectCode);
            console.log("getProjectDetail", res);
            let project = res.data as ProjectItem;
            this.projects[index] = project;
            return project;
        },
        async createProject(project: ProjectItem): Promise<any> {
            const res = await this.createProject(project);
            console.log("createProject", res);
            return res;
        },
        async deleteProject(projectCode: string): Promise<any> {
            const res = await delProjectByCode(projectCode);
            for (let index = 0; index < this.projects.length; index++) {
                if (projectCode == this.projects[index].code) {
                    this.projects.splice(index, 1);
                    break;
                }
            }
            return res;
        },
        async updateProject(project: ProjectItem): Promise<any> {
            const res = await updateProject(project);
            for (let index = 0; index < this.projects.length; index++) {
                if(project.code==this.projects[index].code){
                    this.projects[index].name=project.name;
                    this.projects[index].description=project.description;
                    break;
                }
            }
            return res;
        }
    }
});
