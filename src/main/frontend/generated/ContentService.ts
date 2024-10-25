import { EndpointRequestInit as EndpointRequestInit_1 } from "@vaadin/hilla-frontend";
import client_1 from "./connect-client.default.js";
async function add_1(value: string | undefined, init?: EndpointRequestInit_1): Promise<string | undefined> { return client_1.call("ContentService", "add", { value }, init); }
async function findAll_1(init?: EndpointRequestInit_1): Promise<Array<string | undefined> | undefined> { return client_1.call("ContentService", "findAll", {}, init); }
export { add_1 as add, findAll_1 as findAll };
