"use client"

import { useState } from 'react'
import { toast } from 'react-toastify'
import { z } from 'zod'
import type { AlbumData } from '@/lib/types'

const albumSchema = z.object({
    album_name: z.string().min(1, 'Album name is required'),
    album_title_img: z.instanceof(File, { message: 'Title image is required' }),
})

export default function AddAlbum() {
    const [formData, setFormData] = useState({
        album_name: '',
        album_title_img: null as File | null,
    })

    const [errors, setErrors] = useState<Record<string, string>>({})
    const [previewUrl, setPreviewUrl] = useState<string>('')

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target
        setFormData((prev) => ({ ...prev, [name]: value }))
        // Clear error when user starts typing
        if (errors[name]) {
            setErrors((prev) => ({ ...prev, [name]: '' }))
        }
    }

    const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0]
        if (file) {
            // Check file size (300KB limit)
            if (file.size > 300 * 1024) {
                setErrors((prev) => ({
                    ...prev,
                    album_title_img: 'File size must be less than 300KB',
                }))
                return
            }
            // Check file type
            if (!file.type.startsWith('image/')) {
                setErrors((prev) => ({
                    ...prev,
                    album_title_img: 'Only image files are allowed',
                }))
                return
            }
            setFormData((prev) => ({ ...prev, album_title_img: file }))
            setErrors((prev) => ({ ...prev, album_title_img: '' }))

            // Create preview URL
            const reader = new FileReader()
            reader.onload = () => {
                setPreviewUrl(reader.result as string)
            }
            reader.readAsDataURL(file)
        }
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        try {
            const validatedData = albumSchema.parse(formData)
            // TODO: Submit to API
            console.log('Form data:', validatedData)
            toast.success('Album added successfully')
            setFormData({
                album_name: '',
                album_title_img: null,
            })
            setPreviewUrl('')
        } catch (error) {
            if (error instanceof z.ZodError) {
                const newErrors: Record<string, string> = {}
                error.errors.forEach((err) => {
                    if (err.path[0]) {
                        newErrors[err.path[0].toString()] = err.message
                    }
                })
                setErrors(newErrors)
            }
        }
    }

    return (
        <div className="mx-auto max-w-2xl">
            <h1 className="mb-8 text-center text-3xl font-bold">Add Album</h1>

            <div className="rounded-lg border border-gray-200 bg-white p-6 dark:border-gray-700 dark:bg-gray-800">
                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label className="block text-sm font-medium">
                            Album Name
                            <span className="text-red-500">*</span>
                        </label>
                        <input
                            type="text"
                            name="album_name"
                            value={formData.album_name}
                            onChange={handleChange}
                            className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 dark:border-gray-600 dark:bg-gray-700"
                        />
                        {errors.album_name && (
                            <p className="mt-1 text-sm text-red-500">
                                {errors.album_name}
                            </p>
                        )}
                    </div>

                    <div>
                        <label className="block text-sm font-medium">
                            Album Title Image
                            <span className="text-red-500">*</span>
                        </label>
                        <input
                            type="file"
                            accept="image/*"
                            onChange={handleImageChange}
                            className="mt-1 block w-full rounded-md border border-gray-300 px-3 py-2 text-gray-700 dark:border-gray-600 dark:bg-gray-700 dark:text-gray-300"
                        />
                        {errors.album_title_img && (
                            <p className="mt-1 text-sm text-red-500">
                                {errors.album_title_img}
                            </p>
                        )}
                        <p className="mt-1 text-xs text-gray-500">
                            Only image files up to 300KB are allowed
                        </p>
                    </div>

                    {previewUrl && (
                        <div>
                            <label className="block text-sm font-medium">
                                Preview
                            </label>
                            <div className="mt-2">
                                <img
                                    src={previewUrl}
                                    alt="Preview"
                                    className="h-48 w-48 rounded-lg object-cover"
                                />
                            </div>
                        </div>
                    )}

                    <div className="flex justify-center gap-4">
                        <button
                            type="submit"
                            className="rounded-md bg-blue-600 px-4 py-2 text-white hover:bg-blue-700"
                        >
                            Submit
                        </button>
                        <button
                            type="reset"
                            onClick={() => {
                                setFormData({
                                    album_name: '',
                                    album_title_img: null,
                                })
                                setPreviewUrl('')
                                setErrors({})
                            }}
                            className="rounded-md bg-gray-600 px-4 py-2 text-white hover:bg-gray-700"
                        >
                            Reset
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
} 