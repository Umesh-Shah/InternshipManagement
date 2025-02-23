import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'

export function middleware(request: NextRequest) {
    const token = request.cookies.get('token')?.value
    const { pathname } = request.nextUrl

    // Public paths that don't require authentication
    const publicPaths = ['/login', '/register', '/forgot-password']
    const isPublicPath = publicPaths.some(path => pathname.startsWith(path))

    // Auth paths that should redirect to dashboard if already authenticated
    const authPaths = ['/login', '/register']
    const isAuthPath = authPaths.some(path => pathname.startsWith(path))

    // If the user is on an auth path and is already authenticated,
    // redirect them to the dashboard
    if (isAuthPath && token) {
        return NextResponse.redirect(new URL('/student/dashboard', request.url))
    }

    // If the path is public, allow access
    if (isPublicPath) {
        return NextResponse.next()
    }

    // If the user is not authenticated and the path is not public,
    // redirect to login
    if (!token) {
        const redirectUrl = new URL('/login', request.url)
        redirectUrl.searchParams.set('from', pathname)
        return NextResponse.redirect(redirectUrl)
    }

    return NextResponse.next()
}

export const config = {
    matcher: [
        /*
         * Match all request paths except for the ones starting with:
         * - api (API routes)
         * - _next/static (static files)
         * - _next/image (image optimization files)
         * - favicon.ico (favicon file)
         */
        '/((?!api|_next/static|_next/image|favicon.ico).*)',
    ],
} 