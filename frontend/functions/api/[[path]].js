export async function onRequest(context) {
    const url = new URL(context.request.url);
    const backendUrl = `https://mikie-sportswear-376881510219.northamerica-northeast2.run.app${url.pathname}${url.search}`;

    return fetch(backendUrl, {
        method: context.request.method,
        headers: context.request.headers,
        body: context.request.method !== 'GET' ? context.request.body : undefined,
    });
}