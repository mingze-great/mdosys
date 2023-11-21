<template>
    <div class="select_table_box">
        <div style="padding: 3px 5px;" id="test2">
            <el-input v-model="input" class="w-50 m-2" placeholder="请查找数据" :prefix-icon="Search" style="width: 100%;"
                size="small" />
        </div>
        <div class="rightMenu" v-show="showVisableMenu">
            <ul>
                <li @click="addView()">
                    <el-icon color="rgba(15,100,200,0.9)" :size="16" style="margin-right: 5px">
                        <View />
                    </el-icon>
                    <span class="menu-text">可视化</span>
                </li>
            </ul>
        </div>
        <el-table :data="selectData" style="width: 100%" class="task_table" height="100%" border overflow row-key="id"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" @cell-dblclick="editData"
            @row-contextmenu="rightClick">
            <el-table-column label="参数" min-width="52%" show-overflow-tooltip>
                <template #default="scope">
                    <span class="name-box" draggable="true"
                        @dragstart="dragstart(scope.row.name, scope.row.id, scope.row.index as string, scope.row.unit)"
                        @dragend="dragend">
                        {{ scope.row.name }}</span>
                </template>
            </el-table-column>
            <el-table-column label="数值" min-width="22%" show-overflow-tooltip>
                <template #default="scope">
                    <el-input v-if="scope.row.state == true" v-model="scope.row.index"
                        @blur="alterData(scope.row, scope.column)" class="input-box"></el-input>
                    <span v-else class="name-box">{{ scope.row.index }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="unit" label="单位" min-width="16%" show-overflow-tooltip />
        </el-table>
    </div>
</template>


<script  lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useOptimDesinParam } from '@/store/processes/optimDesign/index';
import { Search } from '@element-plus/icons-vue';
import { ApplicationCore } from '@/ApplicationCore';
import { TabType } from '@/ApplicationCore/TabsManager/TabInterface';
//import menuButton from '@/views/pages/Process/menuIndex.vue'
// function confirm() {
//     props.obj.dialogVisibal = false;
// }
const applicationCore=ApplicationCore.getInstance();
const OptimDesinParam = useOptimDesinParam();

const showVisableMenu = ref(false);

const input = ref()

const rightClick = (row: any, column: any, event: any) => {
    console.log("youjianle", row.id)
    event.preventDefault();
    showVisableMenu.value = true;
    let menu = document.querySelector('.rightMenu')! as HTMLElement;

    menu.style.left = event.clientX + 'px';
    menu.style.top = event.clientY + 'px';
    document.addEventListener('click', show);
    //document.addEventListener('contextmenu', show);
    console.log("menu11", event);

}

const show = () => {
    showVisableMenu.value = false;
}

const addView = () => {
    applicationCore.processTabsManager.addTabView(TabType.EXAMPLE,"数据可视化展示","Id");
}

function dragstart(name: string, code: string, index: string, units: string) {
    console.log("tuodongle", code)
    OptimDesinParam.getTempParam(name, code, index, units)
}

function dragend() {
    console.log("fangxiale")
}

function editData(row: any, column: any, cell: any, event: any) {
    if (column.label == '数值')
        row.state = !row.state;
    console.log("shuangji", row.state);

    let menu = document.querySelector('.rightMenu')! as HTMLElement;
    console.log("menu", event);


}

function alterData(row: any, column: any) {
    console.log("shijiao", row.state);
    row.state = !row.state;
}

const selectData = [
    {
        id: '1',
        name: '燃烧室壳体',
        index: '1000.0',
        unit: 'mm',
        state: false,
        children: [
            {
                name: '内部数据',
                index: '111.0',
                unit: 'mm',
                state: false,
            },
            {
                name: '内部数据',
                index: '111.0',
                unit: 'mm',
                state: false,
            },
            {
                name: '内部数据',
                index: '111.0',
                unit: 'mm',
                state: false,
            },
        ]
    },
    {
        id: '2',
        name: '燃烧室翼长',
        index: '1000.0',
        unit: 'mm',
        state: false,
    },
    {
        id: '3',
        name: '燃烧室翼宽',
        index: '11.0',
        unit: 'mm',
        state: false,
    },
    {
        id: '4',
        name: '燃烧室翼宽',
        index: '123.0',
        unit: 'mm',
        state: false,
        children: [
            {
                name: '内部数据',
                index: '111.0',
                unit: 'mm',
                state: false,
            },
            {
                name: '内部数据',
                index: '111.0',
                unit: 'mm',
                state: false,
            },
        ]
    },
    {
        id: '5',
        name: '燃烧室壳体',
        index: '1000.0',
        unit: 'mm',
        state: false,
    },
    {
        id: '6',
        name: '燃烧室壳体',
        index: '1000.0',
        unit: 'mm',
        state: false,
    },
    {
        id: '7',
        name: '燃烧室壳体',
        index: '1000.0',
        unit: 'mm',
        state: false,
    },
    {
        id: '8',
        name: '燃烧室壳体',
        index: '1000.0',
        unit: 'mm',
        state: false,
    },
    {
        id: '9',
        name: '燃烧室壳体',
        index: '1000.0',
        unit: 'mm',
        state: false,
    },
    {
        id: '10',
        name: '燃烧室壳体',
        index: '1000.0',
        unit: 'mm',
        state: false,
    },
    {
        id: '11',
        name: '燃烧室壳体',
        index: '1000.0',
        unit: 'mm',
        state: false,
    },
    {
        id: '12',
        name: '燃烧室壳体',
        index: '1000.0',
        unit: 'mm',
        state: false,
    },
]

</script>

<style lang="scss">
.select_table_box {
    width: 100%;
    //height: calc(100vh - 170px);
    height: 100%;
    box-sizing: border-box;
    // border: $global_bd_style;
    // border-color: $global_bd_color;
    // border-radius: $global_bd_radius;

    //background-color: bisque;
    .task_table {
        font-size: 12px !important;
        cursor: default;

        ::v-deep .cell {
            white-space: nowrap !important;
        }

        .name-box {
            cursor: move;
            -webkit-user-select: none;
        }
    }

    .task_table th {
        padding: 0;
    }

    .task_table td {
        padding: 0;
    }

    .data-tree {
        .el-tree-node__label {
            font-size: 12px !important;
        }
    }

    .input-box {
        height: 20px !important;
        line-height: 20px !important;
        font-size: 12px !important;
        text-align: center !important;

        //padding: 1px 5px;
        .el-input__wrapper {
            padding: 1px 5px;
        }
    }

}


.rightMenu {
    background: #fff;
    padding: 5px 0;
    width: 80px;
    position: fixed;
    z-index: 99999999;
    cursor: pointer;
    border: $global_bd_style;
    border-color: $global_bd_color;
    box-shadow: $global_bd_shadow;
    border-radius: $global_bd_radius;
    font-size: 12px;
}

.rightMenu ul {
    list-style: none;
    margin: 0;
    padding: 0;
    border-radius: $global_bd_radius;
}

.rightMenu ul li {
    box-sizing: border-box;
    padding: 3px 10px;
    background: #fff;
    // border-bottom: $global_bd_style;
    border-color: $global_bd_color;
    box-sizing: border-box;
    display: flex;
    align-items: center;
}

.rightMenu ul li:last-child {
    border: none;
}

.rightMenu ul li:hover {
    transition: all 1s;
    background: #92c9f6;
}
</style>
