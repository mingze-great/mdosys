import { RouteLocation, RouteLocationNormalized, RouteLocationNormalizedLoaded } from "vue-router";


export function selectPathFromArray(routeLocation: RouteLocation | RouteLocationNormalizedLoaded | RouteLocationNormalized, targetArray: Array<string>,default_route?:string): string {
    let routePath: string ;
    if(default_route){
        routePath = default_route;
    }else{
        routePath = routeLocation.path;
    }
    routeLocation.matched.forEach((item) => {
        if (targetArray.indexOf(item.path) > -1) {
            routePath = item.path;
        }
    });
    return routePath;
}
