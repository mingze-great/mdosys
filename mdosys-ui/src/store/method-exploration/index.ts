import {defineStore} from "pinia";
import {ref, Ref} from "vue";
import {nanoid} from "nanoid";
import dayjs from "dayjs";
import {
    save as Save,
    changeNoteName as ChangeNoteName,
    deleteNotePage as DeleteNotePage,
    getNoteList as GetNoteList,
    addNotePage as AddNotePage,
    exportAsPDF as ExportAsPDF
} from "@/apis/Share/MethodExploration"

export class Note {
    private _onlyKey: string
    private _noteName: Ref<string>
    private _noteNameEditable: Ref<boolean>
    private _noteContent: Ref<string>
    private _recentEditTime: string

    public constructor(noteName: string, noteContent: string, recentEditTime: string) {
        this._onlyKey = nanoid()
        this._noteName = ref(noteName)
        this._noteContent = ref(noteContent)
        this._noteNameEditable = ref(false)
        this._recentEditTime = recentEditTime
    }

    get recentEditTime(): string {
        return this._recentEditTime;
    }

    set recentEditTime(value: string) {
        this._recentEditTime = value;
    }

    get noteContent(): Ref<string> {
        return this._noteContent;
    }

    set noteContent(value: Ref<string>) {
        this._noteContent = value;
    }

    get noteName(): Ref<string> {
        return this._noteName;
    }

    set noteName(value: Ref<string>) {
        this._noteName = value;
    }

    get onlyKey(): string {
        return this._onlyKey;
    }

    set onlyKey(value: string) {
        this._onlyKey = value;
    }

    get noteNameEditable(): Ref<boolean> {
        return this._noteNameEditable;
    }

    set noteNameEditable(value: Ref<boolean>) {
        this._noteNameEditable = value;
    }
}

export const useMethodExplorationStore = defineStore("methodExplorationStore", {
    state: () => {
        return {
            notes: <Note[]>[
                new Note("探索1", "喷气燃动力", "2023-1-16 21:23:10"),
                new Note("探索2", "飞行器结构设计", "2023-1-16 21:23:12")
            ]
        }
    },
    actions: {
        save(key: string): Promise<any> {
            return new Promise<any>((resolve, reject) => {
                let note = this.notes.find(item => item.onlyKey === key)
                Save(note!.onlyKey, note!.noteContent, dayjs(Date.now()).format("YYYY-MM-DD HH:mm:ss"))
                    .then(res => {
                        resolve(res)
                    })
                    .catch(error => {
                        reject(error)
                    })
            })
        },
        changeNoteName(key: string, noteName: string): Promise<any> {
            return new Promise<any>((resolve, reject) => {
                let note = this.notes.find(item => item.onlyKey === key)
                ChangeNoteName(note!.onlyKey, note!.noteContent)
                    .then(res => {
                        resolve(res)
                    })
                    .catch(error => {
                        reject(error)
                    })
            })
        },
        deleteNotePage(key: string): Promise<any> {
            return new Promise<any>((resolve, reject) => {
                DeleteNotePage(key)
                    .then(res => {
                        GetNoteList()
                        resolve(res)
                    })
                    .catch(error => {
                        reject(error)
                    })
            })
        },
        addNotePage(noteName: string): Promise<any> {
            return new Promise((resolve, reject) => {
                AddNotePage(nanoid(), noteName, dayjs(Date.now()).format("YYYY-MM-DD HH:mm:ss"))
                    .then(res => {
                        resolve(res)
                    })
                    .catch(error => {
                        reject(error)
                    })
            })
        },
        getNoteList(): Promise<any> {
            return new Promise((resolve, reject) => {
                GetNoteList()
                    .then(res => {
                        this.notes = res.data.notes
                        resolve(res)
                    })
                    .catch(error => {
                        reject(error)
                    })
            })
        },
        exportAsPDF(htmlStr: string): Promise<any> {
            return new Promise((resolve, reject) => {
                ExportAsPDF(htmlStr)
                    .then(res => {
                        resolve(res)
                    })
                    .catch(error => {
                        reject(error)
                    })
            })
        }
    },
    getters: {
        firstNoteName(): string {
            return this.notes[0].noteName
        },
        lastNoteName(): string {
            return this.notes[this.notes.length - 1].noteName
        }
    }
})