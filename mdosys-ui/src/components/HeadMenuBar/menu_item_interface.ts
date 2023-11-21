interface group_item {
    group_name: string
    key: string,
    children: Array<child_item>
}

interface child_item {
    icon_src: string,
    icon_alt: string,
    content: string,
    key: string
}

interface outer_type {
    item: child_item,
    item_index: number,
    group_index: number,
    group_name: string,
    group_key: string,
}

export type {
    group_item,
    child_item,
    outer_type,
}
