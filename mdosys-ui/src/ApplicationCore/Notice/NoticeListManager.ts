import {ref, Ref, UnwrapRef} from "vue";
import {nanoid} from "nanoid";

export class Notice {
    private readonly _key: string
    private _noticeTitle: string
    private _noticeContent: string
    private _isHandled: boolean
    private _goto: string

    public constructor(noticeTitle: string, noticeContent: string, goto: string) {
        this._key = nanoid()
        this._noticeTitle = noticeTitle
        this._noticeContent = noticeContent
        this._isHandled = false
        this._goto = goto
    }

    get key(): string {
        return this._key;
    }

    get noticeContent(): string {
        return this._noticeContent;
    }

    set noticeContent(value: string) {
        this._noticeContent = value;
    }

    get noticeTitle(): string {
        return this._noticeTitle;
    }

    set noticeTitle(value: string) {
        this._noticeTitle = value;
    }

    get isHandled(): boolean {
        return this._isHandled;
    }

    set isHandled(value: boolean) {
        this._isHandled = value;
    }

    get goto(): string {
        return this._goto;
    }

    set goto(value: string) {
        this._goto = value;
    }
}

export class NoticeListManager {
    private static instance: NoticeListManager | undefined
    private readonly _noticeList: Ref<UnwrapRef<Array<Notice>>>
    private static _noticeListShow: Ref<UnwrapRef<boolean>> = ref(false)

    private constructor() {
        this._noticeList = ref(new Array<Notice>())
        this.addNotice(new Notice("test", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. A accusamus, commodi cupiditate enim numquam sit soluta. Architecto, cumque deleniti fugiat magni maxime molestias necessitatibus nostrum pariatur quae quasi recusandae voluptatem.", "/process"))
        this.addNotice(new Notice("test2", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. A accusamus, commodi cupiditate enim numquam sit soluta. Architecto, cumque deleniti fugiat magni maxime molestias necessitatibus nostrum pariatur quae quasi recusandae voluptatem.", "/process_monitor"))
        this.addNotice(new Notice("test3", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. A accusamus, commodi cupiditate enim numquam sit soluta. Architecto, cumque deleniti fugiat magni maxime molestias necessitatibus nostrum pariatur quae quasi recusandae voluptatem.", "/knowledge_lib/component/manage"))
    }

    public static getInstance() {
        if (this.instance === undefined) {
            this.instance = new NoticeListManager()
        }
        return this.instance
    }

    public addNotice(notice: Notice) {
        this._noticeList.value.push(notice)
    }

    public removeNotice(key: string) {
        let notice = this._noticeList.value.find(item => item.key === key);
        notice!.isHandled = true
    }

    get noticeList(): Ref<UnwrapRef<Notice[]>> {
        return this._noticeList;
    }

    static get noticeListShow(): Ref<UnwrapRef<boolean>> {
        return this._noticeListShow;
    }

    static set noticeListShow(value: Ref<UnwrapRef<boolean>>) {
        this._noticeListShow = value;
    }
}