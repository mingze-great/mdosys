import { ref } from 'vue';
import { createInjectionState } from "@vueuse/core";

const [useProvideParamsStore, useParamsStore] = createInjectionState((initialValue: number) => {
    const params = ref(initialValue);
    return {params};
})

export { useProvideParamsStore };
export { useParamsStore };

export function useParamsStoreWithDefault() {
    return useParamsStore() ?? {
        params: ref(0)
    }
}