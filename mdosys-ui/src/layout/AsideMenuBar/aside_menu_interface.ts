
interface Iitem{
    name: string,
    path: string,
    hidden: boolean,
    alwaysShow: boolean,
    component: string,
    meta: {
      title: string,
      icon: string,
      noCache: boolean,
      link: string|Object
    },
    children?: Array<Iitem>
}

export type {
    Iitem
}
